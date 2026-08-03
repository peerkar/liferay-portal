/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.staging.StagingGroupHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Petteri Karttunen
 */
public class ExportImportSiteProviderImplTest {

	@ClassRule
	@Rule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		_exportImportSiteProviderImpl = new ExportImportSiteProviderImpl();

		_stagingGroupHelper = Mockito.mock(StagingGroupHelper.class);

		ReflectionTestUtil.setFieldValue(
			_exportImportSiteProviderImpl, "_stagingGroupHelper",
			_stagingGroupHelper);
	}

	@Test
	public void testGetDisplayNameWhenGroupIsNull() {
		Assert.assertEquals(
			StringPool.BLANK,
			_exportImportSiteProviderImpl.getDisplayName(null, LocaleUtil.US));
	}

	@Test
	public void testGetDisplayNameWhenNameIsNotWhatItGoesBy() throws Exception {

		// The name a site is stored under is not always the name it goes by.
		// The site an instance starts life with is stored as Guest and the
		// Global site under the ID of its company.

		Group group = _mockSite();

		Mockito.when(
			group.getDescriptiveName(LocaleUtil.US)
		).thenReturn(
			"Liferay DXP Site"
		);

		Assert.assertEquals(
			"Liferay DXP Site",
			_exportImportSiteProviderImpl.getDisplayName(group, LocaleUtil.US));
	}

	@Test
	public void testGetDisplayNameWhenThereIsNoDescriptiveName()
		throws Exception {

		Group group = _mockSite();

		Mockito.when(
			group.getDescriptiveName(LocaleUtil.US)
		).thenThrow(
			new PortalException()
		);

		Mockito.when(
			group.getName(LocaleUtil.US)
		).thenReturn(
			"Guest"
		);

		Assert.assertEquals(
			"Guest",
			_exportImportSiteProviderImpl.getDisplayName(group, LocaleUtil.US));
	}

	@Test
	public void testIsSupportedWhenGroupIsCMS() {
		Group group = _mockSite();

		Mockito.when(
			group.isCMS()
		).thenReturn(
			true
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsCompanyGroup() {

		// The group the instance level export itself runs under is not a site
		// anyone picks

		Group group = _mockSite();

		Mockito.when(
			_stagingGroupHelper.isCompanyGroup(group)
		).thenReturn(
			true
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsDepot() {
		Group group = _mockSite();

		Mockito.when(
			group.isDepot()
		).thenReturn(
			true
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsGlobal() {

		// The Global site is a site like any other here, even though its class
		// name is Company. The group that is left out is the one the instance
		// level export runs under, which is a different group altogether.

		Group group = _mockSite();

		Mockito.when(
			group.isCompany()
		).thenReturn(
			true
		);

		Assert.assertTrue(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsInactive() {
		Group group = _mockSite();

		Mockito.when(
			group.isActive()
		).thenReturn(
			false
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsLayoutPrototype() {
		Group group = _mockSite();

		Mockito.when(
			group.isLayoutPrototype()
		).thenReturn(
			true
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsLayoutSetPrototype() {
		Group group = _mockSite();

		Mockito.when(
			group.isLayoutSetPrototype()
		).thenReturn(
			true
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsNotASite() {
		Group group = _mockSite();

		Mockito.when(
			group.isSite()
		).thenReturn(
			false
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsNull() {
		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(null));
	}

	@Test
	public void testIsSupportedWhenGroupIsSite() {
		Assert.assertTrue(
			_exportImportSiteProviderImpl.isSupported(_mockSite()));
	}

	@Test
	public void testIsSupportedWhenGroupIsStaged() {
		Group group = _mockSite();

		Mockito.when(
			group.isStaged()
		).thenReturn(
			true
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	@Test
	public void testIsSupportedWhenGroupIsStagingGroup() {
		Group group = _mockSite();

		Mockito.when(
			group.isStagingGroup()
		).thenReturn(
			true
		);

		Assert.assertFalse(_exportImportSiteProviderImpl.isSupported(group));
	}

	/**
	 * Returns a site nothing rules out, so that a test only has to say what
	 * makes its own site different.
	 */
	private Group _mockSite() {
		Group group = Mockito.mock(Group.class);

		Mockito.when(
			group.isActive()
		).thenReturn(
			true
		);

		Mockito.when(
			group.isSite()
		).thenReturn(
			true
		);

		return group;
	}

	private ExportImportSiteProviderImpl _exportImportSiteProviderImpl;
	private StagingGroupHelper _stagingGroupHelper;

}