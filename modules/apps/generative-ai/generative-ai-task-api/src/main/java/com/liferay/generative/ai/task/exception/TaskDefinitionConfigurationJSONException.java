/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.exception;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Petteri Karttunen
 */
public class TaskDefinitionConfigurationJSONException extends PortalException {

	public TaskDefinitionConfigurationJSONException() {
	}

	public TaskDefinitionConfigurationJSONException(String msg) {
		super(msg);
	}

	public TaskDefinitionConfigurationJSONException(
		String msg, Throwable throwable) {

		super(msg, throwable);
	}

	public TaskDefinitionConfigurationJSONException(Throwable throwable) {
		super(throwable);
	}

}