/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task.google;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.io.File;

import java.util.List;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

// A demo persistence store for the chat history.

/**
 * @author Petteri Karttunen
 */
public class GeminiMapDBChatMemoryStore implements ChatMemoryStore {

	@Override
	public void deleteMessages(Object memoryId) {
		_map.remove((String)memoryId);
		_db.commit();
	}

	@Override
	public List<ChatMessage> getMessages(Object memoryId) {
		return ChatMessageDeserializer.messagesFromJson(
			_map.get((String)memoryId));
	}

	@Override
	public void updateMessages(Object memoryId, List<ChatMessage> messages) {
		String json = ChatMessageSerializer.messagesToJson(messages);

		_map.put((String)memoryId, json);

		_db.commit();
	}

	private static String _getDBFilePath() {
		return System.getProperty("java.io.tmpdir") + "/gemini-chat-memory.db";
	}

	private static final DB _db;
	private static final Map<String, String> _map;

	static {
		new File(
			_getDBFilePath()
		).delete();

		_db = DBMaker.fileDB(
			_getDBFilePath()
		).closeOnJvmShutdown(
		).transactionEnable(
		).make();

		_map = _db.hashMap(
			"messages", Serializer.STRING, Serializer.STRING
		).createOrOpen();
	}

}