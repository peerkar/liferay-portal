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
import qs from 'qs';
import {useEffect, useRef, useState} from 'react';

const qsOptions = {allowDots: true, arrayFormat: 'bracket'};

export const toQuery = (queryString = '', defaultQuery = {}) => {
	const query = queryString.length
		? qs.parse(queryString.substr(1), qsOptions)
		: {};

	return {...defaultQuery, ...query};
};

export const toQueryString = (query) => {
	return query ? `${qs.stringify(query, qsOptions)}` : '';
};

export default (history, defaultQuery = {}) => {
	const {location} = history;
	const {pathname, search} = location;
	const defaultQueryRef = useRef(defaultQuery);
	const [query, setQuery] = useState(
		toQuery(search, defaultQueryRef.current)
	);

	useEffect(() => {
		setQuery(toQuery(search, defaultQueryRef.current));
	}, [defaultQueryRef, search]);

	return [
		query,
		(query) => history.push(`${pathname}?${toQueryString(query)}`),
	];
};
