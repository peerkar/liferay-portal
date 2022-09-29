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

package com.liferay.search.experiences.ingest.web.internal.ingester;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Petteri Karttunen
 */
@Component(enabled = false, immediate = true, service = IngesterFactory.class)
public class IngesterFactoryImpl implements IngesterFactory {

	@Override
	public Ingester getIngester(String type) throws IllegalArgumentException {
		Ingester ingester = _ingesterServiceTrackerMap.getService(type);

		if (ingester == null) {
			throw new IllegalArgumentException(
				"Unable to find ingester for " + type);
		}

		return ingester;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_ingesterServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, Ingester.class, "type");
	}

	@Deactivate
	protected void deactivate() {
		_ingesterServiceTrackerMap.close();
	}

	private ServiceTrackerMap<String, Ingester> _ingesterServiceTrackerMap;

}