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

package com.liferay.portal.search.tuning.gsearch.impl.internal.clause;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.impl.internal.component.ServiceComponentReference;
import com.liferay.portal.search.tuning.gsearch.spi.clause.ClauseBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = ClauseBuilderFactory.class)
public class ClauseBuilderFactoryImpl implements ClauseBuilderFactory {

	@Override
	public ClauseBuilder getBuilder(String type)
		throws IllegalArgumentException {

		ServiceComponentReference<ClauseBuilder> serviceComponentReference =
			_clauseBuilders.get(type);

		if (serviceComponentReference == null) {
			throw new IllegalArgumentException(
				"No registered clause builder for " + type);
		}

		return serviceComponentReference.getServiceComponent();
	}

	@Override
	public String[] getBuilderTypes() {
		return _clauseBuilders.keySet(
		).toArray(
			new String[0]
		);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void registerClauseBuilder(
		ClauseBuilder clauseBuilder, Map<String, Object> properties) {

		String type = (String)properties.get("type");
		
		if (Validator.isBlank(type)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to add result item builder " +
						clauseBuilder.getClass(
						).getName() + ". Type property empty.");
			}

			return;
		}

		int serviceRanking = GetterUtil.get(
			properties.get("service.ranking"), 0);

		ServiceComponentReference<ClauseBuilder> serviceComponentReference =
			new ServiceComponentReference<>(clauseBuilder, serviceRanking);

		if (_clauseBuilders.containsKey(type)) {
			ServiceComponentReference<ClauseBuilder> previousReference =
				_clauseBuilders.get(type);

			if (previousReference.compareTo(serviceComponentReference) < 0) {
				_clauseBuilders.put(type, serviceComponentReference);
			}
		} else {
			_clauseBuilders.put(type, serviceComponentReference);
		}
	}

	protected void unregisterClauseBuilder(
		ClauseBuilder clauseBuilder, Map<String, Object> properties) {

		String type = (String)properties.get("type");

		if (Validator.isBlank(type)) {
			return;
		}

		_clauseBuilders.remove(type);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClauseBuilderFactoryImpl.class);

	private volatile Map<String, ServiceComponentReference<ClauseBuilder>>
		_clauseBuilders = new ConcurrentHashMap<>();

}