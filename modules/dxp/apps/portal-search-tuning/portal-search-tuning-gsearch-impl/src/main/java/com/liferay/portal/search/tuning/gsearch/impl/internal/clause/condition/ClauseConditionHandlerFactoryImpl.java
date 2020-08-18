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

package com.liferay.portal.search.tuning.gsearch.impl.internal.clause.condition;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.impl.internal.component.ServiceComponentReference;
import com.liferay.portal.search.tuning.gsearch.spi.clause.ClauseConditionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = ClauseConditionHandlerFactory.class)
public class ClauseConditionHandlerFactoryImpl
	implements ClauseConditionHandlerFactory {

	@Override
	public ClauseConditionHandler getHandler(String name)
		throws IllegalArgumentException {

		ServiceComponentReference<ClauseConditionHandler>
			serviceComponentReference = _clauseConditionHandlers.get(name);

		if (serviceComponentReference == null) {
			throw new IllegalArgumentException(
				"No registered clause condition handler for " + name);
		}

		return serviceComponentReference.getServiceComponent();
	}

	@Override
	public String[] getHandlerNames() {
		return _clauseConditionHandlers.keySet(
		).toArray(
			new String[0]
		);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void registerClauseConditionHandler(
		ClauseConditionHandler clauseConditionHandler,
		Map<String, Object> properties) {

		String name = (String)properties.get("name");

		if (Validator.isBlank(name)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to add result item builder " +
						clauseConditionHandler.getClass(
						).getName() + ". Name property empty.");
			}

			return;
		}

		int serviceRanking = GetterUtil.get(
			properties.get("service.ranking"), 0);

		ServiceComponentReference<ClauseConditionHandler>
			serviceComponentReference = new ServiceComponentReference<>(
				clauseConditionHandler, serviceRanking);

		if (_clauseConditionHandlers.containsKey(name)) {
			ServiceComponentReference<ClauseConditionHandler>
				previousReference = _clauseConditionHandlers.get(name);

			if (previousReference.compareTo(serviceComponentReference) < 0) {
				_clauseConditionHandlers.put(name, serviceComponentReference);
			}
		} else {
			_clauseConditionHandlers.put(name, serviceComponentReference);
		}
	}

	protected void unregisterClauseConditionHandler(
		ClauseConditionHandler clauseConditionHandler,
		Map<String, Object> properties) {

		String name = (String)properties.get("name");

		if (Validator.isBlank(name)) {
			return;
		}

		_clauseConditionHandlers.remove(name);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClauseConditionHandlerFactoryImpl.class);

	private volatile Map
		<String, ServiceComponentReference<ClauseConditionHandler>>
			_clauseConditionHandlers = new ConcurrentHashMap<>();

}