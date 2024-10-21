/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.exception;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Brian Wing Shun Chan
 */
public class TaskDefinitionTitleException extends PortalException {

	public TaskDefinitionTitleException() {
	}

	public TaskDefinitionTitleException(String msg) {
		super(msg);
	}

	public TaskDefinitionTitleException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public TaskDefinitionTitleException(Throwable throwable) {
		super(throwable);
	}

}