/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

/**
 * Temporary data. This data should eventually be fetched from the server.
 */

export const QUERY_FRAGMENTS = [
	{
		clauses: [
			{
				configuration: {
					boost: 20,
					field_name: 'gsearch_locations_${context.language_id}',
					query: '${geolocation.city}',
				},
				occur: 'should',
				query_type: 'match',
			},
			{
				configuration: {
					boost: 10,
					field_name: 'gsearch_locations_${context.language_id}',
					query: '${geolocation.country_name}',
				},
				occur: 'should',
				query_type: 'match',
			},
		],
		conditions: [],
		description: {
			en_US:
				'Broadest query catching documents matching any keyword. Title is given more boost among the fields. Query has the neutral boost of 1.0.',
		},
		enabled: true,
		icon: 'vocabulary',
		title: {en_US: 'Matches any keyword'},
	},
	{
		clauses: [
			{
				configuration: {
					boost: 20,
					field_name: 'gsearch_locations_${context.language_id}',
					query: '${geolocation.city}',
				},
				occur: 'should',
				query_type: 'match',
			},
			{
				configuration: {
					boost: 10,
					field_name: 'gsearch_locations_${context.language_id}',
					query: '${geolocation.country_name}',
				},
				occur: 'should',
				query_type: 'match',
			},
		],
		conditions: [],
		description: {
			en_US: 'Boost content last modified within a time frame.',
		},
		enabled: true,
		icon: 'time',
		title: {en_US: 'Freshness'},
	},
	{
		clauses: [
			{
				configuration: {
					boost: 20,
					field_name: 'gsearch_locations_${context.language_id}',
					query: '${geolocation.city}',
				},
				occur: 'should',
				query_type: 'match',
			},
			{
				configuration: {
					boost: 10,
					field_name: 'gsearch_locations_${context.language_id}',
					query: '${geolocation.country_name}',
				},
				occur: 'should',
				query_type: 'match',
			},
		],
		conditions: [],
		description: {
			en_US: "Boost content created closer to user's location.",
		},
		enabled: true,
		icon: 'geolocation',
		title: {en_US: "User's Geolocation"},
	},
];
