/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.task;

import com.liferay.generative.ai.task.task.Task;
import com.liferay.generative.ai.task.task.TaskMemoryCleaner;

// Only handling Gemini for the moment
import com.liferay.generative.ai.task.internal.task.google.GeminiMapDBChatMemoryStore;
import com.liferay.generative.ai.task.internal.task.google.GeminiMessageWindowChatMemory;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = TaskMemoryCleaner.class)
public class TaskMemoryCleanerImpl implements TaskMemoryCleaner {
    
    public void clear(String memoryId) {

        GeminiMessageWindowChatMemory memory = GeminiMessageWindowChatMemory.builder(
			).id(
				memoryId
			).maxMessages(
				200
			).chatMemoryStore(
				new GeminiMapDBChatMemoryStore()
			).build();
		
		memory.clear();

    }
}
