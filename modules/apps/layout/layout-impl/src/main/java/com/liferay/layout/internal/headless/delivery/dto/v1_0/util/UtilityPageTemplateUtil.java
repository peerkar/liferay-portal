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

package com.liferay.layout.internal.headless.delivery.dto.v1_0.util;

import com.liferay.headless.delivery.dto.v1_0.UtilityPageTemplate;
import com.liferay.layout.utility.page.constants.LayoutUtilityPageEntryConstants;
import com.liferay.layout.utility.page.model.LayoutUtilityPageEntry;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;

/**
 * @author Bárbara Cabrera
 */
public class UtilityPageTemplateUtil {

	public static UtilityPageTemplate toUtilityPageTemplate(
		LayoutUtilityPageEntry layoutUtilityPageEntry) {

		return new UtilityPageTemplate() {
			{
				defaultTemplate =
					layoutUtilityPageEntry.isDefaultLayoutUtilityPageEntry();
				externalReferenceCode =
					layoutUtilityPageEntry.getExternalReferenceCode();
				name = layoutUtilityPageEntry.getName();
				type = Type.create(
					_types.get(layoutUtilityPageEntry.getType()));
			}
		};
	}

	private static final Map<Integer, String> _types = HashMapBuilder.put(
		LayoutUtilityPageEntryConstants.Type.ERROR_404.getType(), "Error"
	).put(
		LayoutUtilityPageEntryConstants.Type.TERMS_OF_USE.getType(),
		"TermsOfUse"
	).build();

}