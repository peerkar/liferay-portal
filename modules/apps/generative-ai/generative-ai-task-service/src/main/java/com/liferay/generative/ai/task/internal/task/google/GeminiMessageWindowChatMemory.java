package com.liferay.generative.ai.task.internal.task.google;

import static dev.langchain4j.internal.ValidationUtils.ensureGreaterThanZero;
import static dev.langchain4j.internal.ValidationUtils.ensureNotNull;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This chat memory operates as a sliding window of {@link #maxMessages} messages.
 * It retains as many of the most recent messages as can fit into the window.
 * If there isn't enough space for a new message, the oldest one is evicted.
 * <p>
 * Once added, a {@link SystemMessage} is always retained.
 * Only one {@code SystemMessage} can be held at a time.
 * If a new {@code SystemMessage} with the same content is added, it is ignored.
 * If a new {@code SystemMessage} with different content is added, the previous {@code SystemMessage} is removed.
 * <p>
 * If an {@link AiMessage} containing {@link ToolExecutionRequest}(s) is evicted,
 * the following orphan {@link ToolExecutionResultMessage}(s) are also automatically evicted
 * to avoid problems with some LLM providers (such as OpenAI)
 * that prohibit sending orphan {@code ToolExecutionResultMessage}(s) in the request.
 * <p>
 * The state of chat memory is stored in {@link ChatMemoryStore} ({@link InMemoryChatMemoryStore} is used by default).
 */
public class GeminiMessageWindowChatMemory implements ChatMemory {

	public static Builder builder() {
		return new GeminiMessageWindowChatMemory.Builder();
	}

	public static GeminiMessageWindowChatMemory withMaxMessages(
		int maxMessages) {

		return builder(
		).maxMessages(
			maxMessages
		).build();
	}

	@Override
	public void add(ChatMessage message) {
		List<ChatMessage> messages = messages();

		if (message instanceof SystemMessage) {
			Optional<SystemMessage> systemMessage = findSystemMessage(messages);

			if (systemMessage.isPresent()) {
				if (systemMessage.get(
					).equals(
						message
					)) {

					return; // do not add the same system message
				}

				messages.remove(systemMessage.get()); // need to replace existing system message
			}
		}

		messages.add(message);
		ensureCapacity(messages, maxMessages);
		store.updateMessages(id, messages);
	}

	@Override
	public void clear() {
		store.deleteMessages(id);
	}

	@Override
	public Object id() {
		return id;
	}

	@Override
	public List<ChatMessage> messages() {
		List<ChatMessage> messages = new LinkedList<>(store.getMessages(id));

		ensureCapacity(messages, maxMessages);

		return messages;
	}

	public static class Builder {

		public GeminiMessageWindowChatMemory build() {
			return new GeminiMessageWindowChatMemory(this);
		}

		/**
		 * @param store The chat memory store responsible for storing the chat memory state.
		 *              If not provided, an {@link InMemoryChatMemoryStore} will be used.
		 * @return builder
		 */
		public Builder chatMemoryStore(ChatMemoryStore store) {
			this.store = store;

			return this;
		}

		/**
		 * @param id The ID of the {@link ChatMemory}.
		 *           If not provided, a "default" will be used.
		 * @return builder
		 */
		public Builder id(Object id) {
			this.id = id;

			return this;
		}

		/**
		 * @param maxMessages The maximum number of messages to retain.
		 *                    If there isn't enough space for a new message, the oldest one is evicted.
		 * @return builder
		 */
		public Builder maxMessages(Integer maxMessages) {
			this.maxMessages = maxMessages;

			return this;
		}

		private Object id = "default";
		private Integer maxMessages;
		private ChatMemoryStore store = new InMemoryChatMemoryStore();

	}

	private GeminiMessageWindowChatMemory(Builder builder) {
		this.id = ensureNotNull(builder.id, "id");
		this.maxMessages = ensureGreaterThanZero(
			builder.maxMessages, "maxMessages");
		this.store = ensureNotNull(builder.store, "store");
	}

	private void ensureCapacity(List<ChatMessage> messages, int maxMessages) {
		while (messages.size() > maxMessages) {
			int messageToEvictIndex = 0;

			if (messages.get(0) instanceof SystemMessage) {
				messageToEvictIndex = 1;
			}

			ChatMessage evictedMessage = messages.remove(messageToEvictIndex);

			//log.trace("Evicting the following message to comply with the capacity requirement: {}", evictedMessage);
			log.trace(
				"Evicting the following message to comply with the capacity requirement: " +
					evictedMessage);

			if ((evictedMessage instanceof AiMessage) &&
				((AiMessage)evictedMessage).hasToolExecutionRequests()) {

				while ((messages.size() > messageToEvictIndex) &&
					   (messages.get(messageToEvictIndex) instanceof
						   ToolExecutionResultMessage)) {

					// Some LLMs (e.g. OpenAI) prohibit ToolExecutionResultMessage(s) without corresponding AiMessage,
					// so we have to automatically evict orphan ToolExecutionResultMessage(s) if AiMessage was evicted

					ChatMessage orphanToolExecutionResultMessage =
						messages.remove(messageToEvictIndex);

					// log.trace("Evicting orphan {}", orphanToolExecutionResultMessage);

					log.trace(
						"Evicting orphan " + orphanToolExecutionResultMessage);
				}
			}
		}
	}

	private Optional<SystemMessage> findSystemMessage(
		List<ChatMessage> messages) {

		return messages.stream(
		).filter(
			message -> message instanceof SystemMessage
		).map(
			message -> (SystemMessage)message
		).findAny();
	}

	private static final Log log = LogFactoryUtil.getLog(
		dev.langchain4j.memory.chat.MessageWindowChatMemory.class);

	private final Object id;
	private final Integer maxMessages;
	private final ChatMemoryStore store;

}