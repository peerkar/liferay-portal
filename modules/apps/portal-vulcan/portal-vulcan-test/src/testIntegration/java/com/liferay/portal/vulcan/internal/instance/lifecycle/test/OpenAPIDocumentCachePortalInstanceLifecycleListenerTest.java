/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.vulcan.internal.instance.lifecycle.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.vulcan.internal.cache.test.BaseOpenAPIDocumentCacheTestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Petteri Karttunen
 */
@RunWith(Arquillian.class)
public class OpenAPIDocumentCachePortalInstanceLifecycleListenerTest
	extends BaseOpenAPIDocumentCacheTestCase {

	@Test
	public void testPopulateOpenAPIDocumentCache() throws Exception {
		ReflectionTestUtil.invoke(
			_portalInstanceLifecycleListener, "_populateOpenAPIDocumentCache",
			new Class<?>[] {long.class}, TestPropsValues.getCompanyId());

		Assert.assertEquals(1, testResource.getCount());

		Company company = _companyLocalService.getCompany(
			TestPropsValues.getCompanyId());

		Assert.assertEquals(
			"{\"openapi\": \"1\"}",
			getContentString(
				company.getPortalURL(0) + _portal.getPathContext() +
					Portal.PATH_MODULE));

		Assert.assertEquals(1, testResource.getCount());
	}

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private Portal _portal;

	@Inject(
		filter = "component.name=com.liferay.portal.vulcan.internal.instance.lifecycle.OpenAPIDocumentCachePortalInstanceLifecycleListener"
	)
	private PortalInstanceLifecycleListener _portalInstanceLifecycleListener;

}