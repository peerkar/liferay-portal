/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.task.context;

import com.liferay.petra.lang.CentralizedThreadLocal;

/**
 * @author Petteri Karttunen
 */
public class TaskContextThreadLocal {

	public static TaskContext getTaskContext() {

		return _taskContext.get();
	}

	public static void setTaskContext(TaskContext taskContext) {
		_taskContext.set(taskContext);
	}

	private static final ThreadLocal<TaskContext> _taskContext =
		new CentralizedThreadLocal<>(
			TaskContextThreadLocal.class + "._taskContext");

}