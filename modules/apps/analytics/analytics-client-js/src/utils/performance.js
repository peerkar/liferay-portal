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

/**
 * Get Duration between marks
 * @param {string} measureName
 * @param {string} startMark
 * @param {string} [endMark=undefined]
 * @returns {number}
 */
export function getDuration(measureName, startMark, endMark = undefined) {
	window.performance.measure(measureName, startMark, endMark);

	const {duration} = window.performance.getEntriesByName(measureName).pop();

	return ~~duration;
}

/**
 * Create mark
 * @param {string} markName
 */
export function createMark(markName) {
	window.performance.clearMarks(markName);
	window.performance.mark(markName);
}
