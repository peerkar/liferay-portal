/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.exception;

import com.liferay.portal.kernel.exception.NoSuchModelException;

/**
 * @author Brian Wing Shun Chan
 */
public class NoSuchTaskDefinitionException extends NoSuchModelException {

	public NoSuchTaskDefinitionException() {
	}

	public NoSuchTaskDefinitionException(String msg) {
		super(msg);
	}

	public NoSuchTaskDefinitionException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public NoSuchTaskDefinitionException(Throwable throwable) {
		super(throwable);
	}

}