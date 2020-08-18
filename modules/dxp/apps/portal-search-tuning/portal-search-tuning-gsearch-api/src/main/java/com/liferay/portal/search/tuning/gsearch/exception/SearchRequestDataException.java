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

package com.liferay.portal.search.tuning.gsearch.exception;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.search.tuning.gsearch.message.Message;

import java.util.List;

/**
 * @author Petteri Karttunen
 */
public class SearchRequestDataException extends PortalException {

	public SearchRequestDataException() {
	}

	public SearchRequestDataException(String msg, List<Message> messages) {
		super(msg);
		_messages = messages;
	}

	public SearchRequestDataException(String msg) {
		super(msg);
	}

	public SearchRequestDataException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SearchRequestDataException(Throwable cause) {
		super(cause);
	}

	public List<Message> getMessages() {
		return _messages;
	}

	private static final long serialVersionUID = 1L;

	private List<Message> _messages;

}