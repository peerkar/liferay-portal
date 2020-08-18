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

package com.liferay.portal.search.tuning.gsearch.message;

import java.io.Serializable;

/**
 * @author Petteri Karttunen
 */
public class Message implements Serializable {

	public Message(Severity severity, String sourceModule, String messageKey) {
		this(severity, sourceModule, messageKey, null, null, null, null, null);
	}

	public Message(
		Severity severity, String sourceModule, String messageKey,
		Object rootObject, String rootProperty, String rootValue) {

		this(
			severity, sourceModule, messageKey, null, null, rootObject,
			rootProperty, rootValue);
	}

	public Message(
		Severity severity, String sourceModule, String messageKey,
		String exceptionMessage, Object exception, Object rootObject,
		String rootProperty, String rootValue) {

		_severity = severity;
		_sourceModule = sourceModule;
		_messageKey = messageKey;
		_exceptionMessage = exceptionMessage;
		_exception = exception;
		_rootObject = rootObject;
		_rootProperty = rootProperty;
		_rootValue = rootValue;
	}

	public Object getException() {
		return _exception;
	}

	public String getExceptionMessage() {
		return _exceptionMessage;
	}

	public String getMessageKey() {
		return _messageKey;
	}

	public Object getRootObject() {
		return _rootObject;
	}

	public String getRootProperty() {
		return _rootProperty;
	}

	public String getRootValue() {
		return _rootValue;
	}

	public Severity getSeverity() {
		return _severity;
	}

	public String getSourceModule() {
		return _sourceModule;
	}

	private static final long serialVersionUID = 1L;

	private Object _exception;
	private String _exceptionMessage;
	private String _messageKey;
	private Object _rootObject;
	private String _rootProperty;
	private String _rootValue;
	private Severity _severity;
	private String _sourceModule;

}