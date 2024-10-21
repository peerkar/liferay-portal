/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.exception;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Petteri Karttunen
 */
public class TaskTestException extends PortalException {

	public TaskTestException() {
	}

	public TaskTestException(String msg) {
		super(msg);
	}

	public TaskTestException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public TaskTestException(Throwable throwable) {
		super(throwable);
	}

}