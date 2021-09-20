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

package com.liferay.search.experiences.internal.blueprint.condition;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.search.experiences.blueprint.clause.ConditionHandler;

import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = ConditionHandlerFactory.class)
public class ConditionHandlerFactoryImpl implements ConditionHandlerFactory {

	@Override
	public ConditionHandler getHandler(String name)
		throws IllegalArgumentException {

		ConditionHandler conditionHandler =
			_conditionHandlerServiceTrackerMap.getService(name);

		if (conditionHandler == null) {
			throw new IllegalArgumentException(
				"No registered condition handler " + name);
		}

		return conditionHandler;
	}

	@Override
	public String[] getHandlerNames() {
		Set<String> keySet = _conditionHandlerServiceTrackerMap.keySet();

		return keySet.toArray(new String[0]);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_conditionHandlerServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ConditionHandler.class, "name");
	}

	@Deactivate
	protected void deactivate() {
		_conditionHandlerServiceTrackerMap.close();
	}

	private ServiceTrackerMap<String, ConditionHandler>
		_conditionHandlerServiceTrackerMap;

}