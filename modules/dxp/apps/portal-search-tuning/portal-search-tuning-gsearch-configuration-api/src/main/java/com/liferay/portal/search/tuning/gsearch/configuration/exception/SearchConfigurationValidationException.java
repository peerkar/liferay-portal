/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.gsearch.configuration.exception;

import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Petteri Karttunen
 */
public class SearchConfigurationValidationException extends PortalException {

	public SearchConfigurationValidationException() {
	}

	public SearchConfigurationValidationException(List<String> errors) {
		super(StringUtil.merge(errors, ","));

		_errors = errors;
	}

	public SearchConfigurationValidationException(String msg) {
		super(msg);
	}

	public SearchConfigurationValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public List<String> getErrors() {
		return _errors;
	}

	private List<String> _errors;

}