/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import ClayCard from '@clayui/card';
import { Text } from '@clayui/core';
import { ClayInput } from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import { fetch } from 'frontend-js-web';
import React, { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react';
import ReactMarkdown from 'react-markdown';

function Message({ content, sender }) {
	const [imageURL, setImageURL] = useState(null);

	if (content.image) {
		if (content.image.base64Data) {
			const base64DataString = "data:image/png;base64, " + content.image.base64Data;

			fetch(base64DataString)
				.then(response => response.blob())
				.then(blob => {
					setImageURL(URL.createObjectURL(blob));
				});
		}
	}

	return (
		<div className="ray-assistant__message">
			<Text truncate weight="semi-bold">
				{sender}:
			</Text>

			{content.text 
				&& <ReactMarkdown>{content.text}</ReactMarkdown> 
			}

			{imageURL
				&& <div className='ai-generated-image-container'>
					<a href={imageURL} target='_blank'>
						<img alt='AI-generated Image' className='ai-generated-image' src={imageURL} />
					</a>
				</div>
			}
		</div>
	);
}

const AssistantChat = forwardRef(function AssistantChat({
	assistantName = 'Assistant',
	endpoints,
	greetingMessage = 'Hi, I am your assistant. How can I help you?',
	taskExternalReferenceCode,
}, ref) {
	const [sidebarBodyHeight, setSidebarBodyHeight] = useState(802);

	const [isWaitingForResponse, setIsWaitingForResponse] = useState(false);

	const [chatHistory, setChatHistory] = useState([]);

	const [prompt, setPrompt] = useState('');

	const originalTaskExternalReferenceCode = useRef(taskExternalReferenceCode);

	const chatInputRef = useRef();

	const clearChatHistory = async () => {
		setChatHistory([]);
		localStorage.removeItem(taskExternalReferenceCode);
		try {
			const response = await fetch(endpoints.sendClearMessagesEndpoint, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
			});

			if (response.status >= 400) {
				handleError(
					`HTTP Error: ${response?.status}${
						response?.statusText ? ', ' + response.statusText : ''}.`
				);
			}

		}
		catch (error) {
			handleError(
				'An error occurred while sending the message.'
			);
		}		
		focusChatInput();
	}

	const focusChatInput = () => {
		if (chatInputRef.current) {
			chatInputRef.current.focus();
		}
	};

	function renameLocalStorageKey(oldKey, newKey) {
		const value = localStorage.getItem(oldKey);
		localStorage.setItem(newKey, value);
		localStorage.removeItem(oldKey);
	}

	useImperativeHandle(ref, () => ({
		clearChatHistory,
		focusChatInput,
	}));

	const isInsideIframe = window.self !== window.top;

	const controlMenu = document.getElementById(
		'_com_liferay_ray_assistant_web_internal_portlet_RayAssistantPortlet_ControlMenu'
	);

	if (isInsideIframe && controlMenu) {
		controlMenu.style.display = 'none';
	}

	const cleanupChatInput = () => {
		const chatInput = document.querySelector('#rayAssistantChatInput');

		if (chatInput) {
			chatInput.value = '';
		};
		setPrompt('');
	};

	useEffect(() => {
		const sidebarElement = document.querySelector('.preview-sidebar.sidebar.sidebar-light');

		if (sidebarElement) {
			setSidebarBodyHeight(sidebarElement.offsetHeight - 64);
		}

		const localStorageChatHistory = localStorage.getItem(taskExternalReferenceCode);

		if (localStorageChatHistory) {
			setChatHistory(JSON.parse(localStorageChatHistory));
		};
	}, []);

	useEffect(() => {
		const messageBody = document.getElementById(
			'rayAssistantConversationContainer'
		);

		if (messageBody) {
			messageBody.scrollTop =
				messageBody.scrollHeight - messageBody.clientHeight;
		}

		if (chatHistory.length) {
			const localStorageChatHistory = chatHistory.map((message) => {
				const localStorageMessage = {...message};

				// console.log("localStorageMessage", localStorageMessage);

				if (localStorageMessage?.image) {
					localStorageMessage.text = "Generated images are not stored in preview chat history.";

					delete localStorageMessage.image;
				}

				return localStorageMessage;
			});

			localStorage.setItem(
				taskExternalReferenceCode,
				JSON.stringify(localStorageChatHistory)
			);
		}
	}, [chatHistory]);

	useEffect(() => {
		if (taskExternalReferenceCode !== originalTaskExternalReferenceCode.current) {
			renameLocalStorageKey(originalTaskExternalReferenceCode.current, taskExternalReferenceCode);
			originalTaskExternalReferenceCode.current = taskExternalReferenceCode;
		}
	}, [taskExternalReferenceCode])

	const handleReceiveMessage = ({output}) => {
		const {image, text} = output;
		
		// console.log('value', output);

		if (output) {
			setChatHistory((currentHistory) => [
				...currentHistory,
				{
					...(text && {text}),
					...(image && {image}),
					role: 'AI',
				},
			]);
		}
		setIsWaitingForResponse(false);
		focusChatInput();
	};

	const handleError = (errorMessage) => {
		handleReceiveMessage(
			{
				output: {
					text: errorMessage,
				},
			}
		)
	};

	const handleSendMessage = async (value) => {
		const oldHistory = chatHistory;

		if (value) {
			setChatHistory((currentHistory) => {
				return [
					...currentHistory,
					{
						text: value,
						role: 'USER',
					},
				]
			});
			setIsWaitingForResponse(true);

			try {
				const response = await fetch(endpoints.sendMessageEndpoint, {
					body: JSON.stringify({ input: { text: value, ...(oldHistory.length && { history: oldHistory }) } }),
					method: 'POST',
					headers: {
						'Content-Type': 'application/json',
					},
				});

				if (response.status !== 200) {
					handleError(
						`HTTP Error: ${response?.status}${response?.statusText ? ', ' + response.statusText : ''}.`
					);
				}
				else {
					const json = await response.json();

					if (json.error) {
						handleReceiveMessage(
							`Request was succesful, but returned an error: ${json.error}`
						);
					}
					else {
						handleReceiveMessage(json);
					}
				}
			}
			catch (error) {
				handleError(
					'An error occurred while sending the message.'
				);
			}
		}
	};

	return (
		<div
			id="rayAssistantRootElement"
			style={{
				height: sidebarBodyHeight,
			}}
		>
			<ClayCard className="ray-assistant__card">
				<ClayCard.Body className="ray-assistant__card-body">
					<div id="rayAssistantContainer">
						<div id="rayAssistantConversationContainer">
							<Message content={{text: greetingMessage}} key={0} sender={assistantName} />

							{chatHistory && chatHistory.length !== 0 && chatHistory.map((historyEntry, index) => (
								<Message
									content={historyEntry}
									key={index + 1}
									sender={historyEntry.role === 'AI' ?
										assistantName
										: Liferay.ThemeDisplay.getUserName()}
								/>
							))}
						</div>

						<div id="rayAssistantChatInputContainer">
							<ClayInput.Group className="input-components">
								<ClayInput.GroupItem className="input-components">
									<ClayInput
										className="input-components input-group-inset input-group-inset-after"
										component="textarea"
										disabled={isWaitingForResponse}
										id="rayAssistantChatInput"
										onBlur={({ target }) => {
											setPrompt(target.value);
										}}
										onKeyDown={(
											event
										) => {
											const inputTarget =
												event.target;
											if (
												event.key === 'Enter' &&
												event.ctrlKey
											) {
												handleSendMessage(
													inputTarget.value
												);
												cleanupChatInput();
											}
										}}
										placeholder={isWaitingForResponse ? `${assistantName} is generating a response...` : "Type a message..."}
										ref={chatInputRef}
										type="text"
									/>

									<ClayInput.GroupInsetItem
										after
										className="input-components inset-item"
									>
										<ClayButton
											displayType="unstyled"
											id="rayAssistantChatInputSubmitButton"
											onClick={(
											) => {
												handleSendMessage(prompt);
												cleanupChatInput();
											}}
										>
											{isWaitingForResponse ? <ClayLoadingIndicator
												displayType="secondary"
												size="sm"
											/> : <ClayIcon symbol="arrow-right-full" />}
										</ClayButton>
									</ClayInput.GroupInsetItem>
								</ClayInput.GroupItem>
							</ClayInput.Group>
						</div>
					</div>
				</ClayCard.Body>
			</ClayCard>
		</div>
	);
});

export default AssistantChat;