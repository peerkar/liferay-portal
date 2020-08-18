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

package com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.impl.internal.component.ServiceComponentReference;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.AggregationBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = AggregationBuilderFactory.class)
public class AggregationBuilderFactoryImpl
	implements AggregationBuilderFactory {

	@Override
	public AggregationBuilder getBuilder(String type)
		throws IllegalArgumentException {

		ServiceComponentReference<AggregationBuilder>
			serviceComponentReference = _aggregationBuilders.get(type);

		if (serviceComponentReference == null) {
			throw new IllegalArgumentException(
				"Unable to find aggregation builder for " + type);
		}

		return serviceComponentReference.getServiceComponent();
	}

	@Override
	public String[] getBuilderTypes() {
		return _aggregationBuilders.keySet(
		).toArray(
			new String[0]
		);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void registerAggregationBuilder(
		AggregationBuilder aggregationBuilder, Map<String, Object> properties) {

		String type = (String)properties.get("type");

		if (Validator.isBlank(type)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to add aggregation builder " +
						aggregationBuilder.getClass(
						).getName() + ". Type property empty.");
			}

			return;
		}

		int serviceRanking = GetterUtil.get(
			properties.get("service.ranking"), 0);

		ServiceComponentReference<AggregationBuilder>
			serviceComponentReference = new ServiceComponentReference<>(
				aggregationBuilder, serviceRanking);

		if (_aggregationBuilders.containsKey(type)) {
			ServiceComponentReference<AggregationBuilder> previousReference =
				_aggregationBuilders.get(type);

			if (previousReference.compareTo(serviceComponentReference) < 0) {
				_aggregationBuilders.put(type, serviceComponentReference);
			}
		} else {
			_aggregationBuilders.put(type, serviceComponentReference);
		}
	}

	protected void unregisterAggregationBuilder(
		AggregationBuilder aggregationBuilder, Map<String, Object> properties) {

		String type = (String)properties.get("type");

		if (Validator.isBlank(type)) {
			return;
		}

		_aggregationBuilders.remove(type);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AggregationBuilderFactoryImpl.class);

	private volatile Map<String, ServiceComponentReference<AggregationBuilder>>
		_aggregationBuilders = new ConcurrentHashMap<>();

}