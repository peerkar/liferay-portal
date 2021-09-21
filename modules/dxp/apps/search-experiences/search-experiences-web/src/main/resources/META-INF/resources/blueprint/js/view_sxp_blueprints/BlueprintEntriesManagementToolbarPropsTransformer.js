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

import {postForm} from 'frontend-js-web';

import openAddBlueprintModal from './OpenAddBlueprintModalCommand';

export default function propsTransformer({
	additionalProps: {deleteBlueprintURL},
	portletNamespace,
	...otherProps
}) {
	const addBlueprint = (itemData) => {
		openAddBlueprintModal({
			contextPath: itemData.contextPath,
			defaultLocale: itemData.defaultLocale,
			dialogTitle: Liferay.Language.get('new-search-blueprint'),
			formSubmitURL: itemData.editBlueprintURL,
			keywordQueryContributorsString:
				itemData.keywordQueryContributorsString,
			modelPrefilterContributorsString:
				itemData.modelPrefilterContributorsString,
			namespace: portletNamespace,
			queryPrefilterContributorsString:
				itemData.queryPrefilterContributorsString,
			searchableAssetTypesString: itemData.searchableAssetTypesString,
			type: itemData.type,
		});
	};

	var deleteEntries = function () {
		if (
			confirm(
				Liferay.Language.get(
					'are-you-sure-you-want-to-delete-blueprints'
				)
			)
		) {
			const form = document.getElementById(`${portletNamespace}fm`);

			const searchContainer = document.getElementById(
				`${portletNamespace}blueprintEntries`
			);

			if (form && searchContainer) {
				postForm(form, {
					data: {
						actionFormInstanceIds: Liferay.Util.listCheckedExcept(
							searchContainer,
							`${portletNamespace}allRowIds`
						),
					},
					url: deleteBlueprintURL,
				});
			}
		}
	};

	return {
		...otherProps,
		onActionButtonClick: (event, {item}) => {
			const action = item?.data?.action;

			if (action === 'deleteEntries') {
				deleteEntries();
			}
		},
		onCreateButtonClick: (event, {item}) => {
			const data = item?.data;

			addBlueprint(data);
		},
	};
}
