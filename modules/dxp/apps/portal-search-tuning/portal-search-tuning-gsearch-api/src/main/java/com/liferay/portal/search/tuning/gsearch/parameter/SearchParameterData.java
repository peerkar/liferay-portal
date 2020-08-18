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

package com.liferay.portal.search.tuning.gsearch.parameter;

import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Petteri Karttunen
 */
public class SearchParameterData {
	
	public void addMessage(Message message) {
		_messages.add(message);
	}
	
	public void addParameter(Parameter parameter) {
		_parameters.add(parameter);
	}
	
	public Optional<Parameter> getByConfigurationVariableName(String name) {
		return _parameters.stream(
		).filter(
			p -> name.equals(p.getConfigurationVariable())
		).findFirst();
	}
	
	public Optional<Parameter> getByName(String name) {
		return _parameters.stream(
		).filter(
			p -> name.equals(p.getName())
		).findFirst();
	}
	
	public Optional<Parameter> getByRole(String role) {
		return _parameters.stream(
		).filter(
			p -> role.equals(
				p.getRoleOptional(
				).orElse(
					""
				))
		).findFirst();
	}

	public List<Message> getMessages() {
		return _messages;
	}

	public List<Parameter> getParameters() {
		return _parameters;
	}

	public boolean hasErrors() {
		return _messages.stream(
		).anyMatch(
			m -> m.getSeverity(
			).equals(
				Severity.ERROR
			)
		);
	}
	
	public boolean hasParameters() {
		return !_parameters.isEmpty();
	}

	private final List<Message> _messages = new ArrayList<>();
	private final List<Parameter> _parameters = new ArrayList<>();

}