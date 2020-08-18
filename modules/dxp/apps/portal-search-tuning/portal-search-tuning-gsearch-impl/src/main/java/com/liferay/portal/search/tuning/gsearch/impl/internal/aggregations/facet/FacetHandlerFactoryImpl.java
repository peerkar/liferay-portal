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

package com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.facet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.impl.internal.component.ServiceComponentReference;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.facet.FacetHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Facet processor factory implementation.
 *
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = FacetHandlerFactory.class)
public class FacetHandlerFactoryImpl implements FacetHandlerFactory {

	@Override
	public FacetHandler getHandler(String name)
		throws IllegalArgumentException {

		ServiceComponentReference<FacetHandler> serviceComponentReference =
			_facetHandlers.get(name);

		if (serviceComponentReference == null) {
			throw new IllegalArgumentException(
				"No registered facet handler for " + name);
		}

		return serviceComponentReference.getServiceComponent();
	}

	@Override
	public String[] getHandlerNames() {
		return _facetHandlers.keySet(
		).toArray(
			new String[0]
		);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void registerFacetHandler(
		FacetHandler facetHandler, Map<String, Object> properties) {

		String name = (String)properties.get("name");

		if (Validator.isBlank(name)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to add facet handler " +
						facetHandler.getClass(
						).getName() + ". Name property empty.");
			}

			return;
		}

		int serviceRanking = GetterUtil.get(
			properties.get("service.ranking"), 0);

		ServiceComponentReference<FacetHandler> serviceComponentReference =
			new ServiceComponentReference<>(facetHandler, serviceRanking);

		if (_facetHandlers.containsKey(name)) {
			ServiceComponentReference<FacetHandler> previousReference =
				_facetHandlers.get(name);

			if (previousReference.compareTo(serviceComponentReference) < 0) {
				_facetHandlers.put(name, serviceComponentReference);
			}
		} else {
			_facetHandlers.put(name, serviceComponentReference);
		}
	}

	protected void unregisterFacetHandler(
		FacetHandler facetHandler, Map<String, Object> properties) {

		String name = (String)properties.get("name");

		if (Validator.isBlank(name)) {
			return;
		}

		_facetHandlers.remove(name);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FacetHandlerFactoryImpl.class);

	private volatile Map<String, ServiceComponentReference<FacetHandler>>
		_facetHandlers = new ConcurrentHashMap<>();

}