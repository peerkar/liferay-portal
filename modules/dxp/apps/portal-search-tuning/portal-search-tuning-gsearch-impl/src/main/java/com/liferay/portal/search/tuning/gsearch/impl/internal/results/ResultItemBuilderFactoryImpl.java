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

package com.liferay.portal.search.tuning.gsearch.impl.internal.results;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.impl.internal.component.ServiceComponentReference;
import com.liferay.portal.search.tuning.gsearch.impl.internal.results.item.builder.DefaultItemBuilder;
import com.liferay.portal.search.tuning.gsearch.spi.results.item.ResultItemBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = ResultItemBuilderFactory.class)
public class ResultItemBuilderFactoryImpl implements ResultItemBuilderFactory {

	@Override
	public ResultItemBuilder getBuilder(String type) {
		ServiceComponentReference<ResultItemBuilder> serviceComponentReference =
			_resultItemBuilders.get(type);

		if (serviceComponentReference == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to find result item builder for " + type +
						". Falling back to default.");
			}

			return new DefaultItemBuilder();
		}

		return serviceComponentReference.getServiceComponent();
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void registerResultItemBuilder(
		ResultItemBuilder resultItemBuilder, Map<String, Object> properties) {

		String type = (String)properties.get("model.class.name");

		if (Validator.isBlank(type)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to add result item builder " +
						resultItemBuilder.getClass(
						).getName() + ". model.class.name property empty.");
			}

			return;
		}

		int serviceRanking = GetterUtil.get(
			properties.get("service.ranking"), 0);

		ServiceComponentReference<ResultItemBuilder> serviceComponentReference =
			new ServiceComponentReference<>(resultItemBuilder, serviceRanking);

		if (_resultItemBuilders.containsKey(type)) {
			ServiceComponentReference<ResultItemBuilder> previousReference =
				_resultItemBuilders.get(type);

			if (previousReference.compareTo(serviceComponentReference) < 0) {
				_resultItemBuilders.put(type, serviceComponentReference);
			}
		} else {
			_resultItemBuilders.put(type, serviceComponentReference);
		}
	}

	protected void unregisterResultItemBuilder(
		ResultItemBuilder resultItemBuilder, Map<String, Object> properties) {

		String type = (String)properties.get("model.class.name");

		if (Validator.isBlank(type)) {
			return;
		}

		_resultItemBuilders.remove(type);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ResultItemBuilderFactoryImpl.class);

	private volatile Map<String, ServiceComponentReference<ResultItemBuilder>>
		_resultItemBuilders = new ConcurrentHashMap<>();

}