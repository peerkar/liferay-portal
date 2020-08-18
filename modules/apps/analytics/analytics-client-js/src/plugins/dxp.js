/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {
	MARK_LOAD_EVENT_START,
	MARK_NAVIGATION_START,
	MARK_PAGE_LOAD_TIME,
	MARK_VIEW_DURATION,
} from '../utils/constants';

const pageApplicationId = 'Page';

/**
 * Plugin function that registers listeners related to DXP
 * @param {Object} analytics The Analytics client
 */

function dxp(analytics) {

	/**
	 * Sends view duration information on the window unload event
	 */
	function beforeNavigate() {
		window.performance.measure(MARK_VIEW_DURATION, MARK_NAVIGATION_START);

		const {duration} = window.performance
			.getEntriesByName(MARK_VIEW_DURATION)
			.pop();

		const props = {
			viewDuration: ~~duration,
		};

		analytics.send('pageUnloaded', pageApplicationId, props);

		window.performance.mark(MARK_NAVIGATION_START);
		window.Liferay.detach('beforeNavigate', beforeNavigate);
	}

	/**
	 * Sends page load information on the endNavigate event when SPA is enabled on DXP
	 */
	function endNavigate() {
		window.performance.mark(MARK_LOAD_EVENT_START);

		window.performance.measure(
			MARK_PAGE_LOAD_TIME,
			MARK_NAVIGATION_START,
			MARK_LOAD_EVENT_START
		);

		const {duration} = window.performance
			.getEntriesByName(MARK_PAGE_LOAD_TIME)
			.pop();

		const props = {
			pageLoadTime: ~~duration,
		};

		analytics.send('pageLoaded', pageApplicationId, props);

		window.Liferay.detach('endNavigate', endNavigate);
	}

	if (window.Liferay && window.Liferay.SPA) {
		window.performance.mark(MARK_NAVIGATION_START);
		window.performance.mark(MARK_LOAD_EVENT_START);

		window.Liferay.on('beforeNavigate', beforeNavigate);
		window.Liferay.on('endNavigate', endNavigate);
	}
}

export {dxp};
export default dxp;
