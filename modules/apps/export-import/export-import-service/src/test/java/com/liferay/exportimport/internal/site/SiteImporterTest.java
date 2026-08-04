/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.site;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataContextFactory;
import com.liferay.exportimport.site.ExportImportSiteProvider;
import com.liferay.exportimport.site.LARSite;
import com.liferay.exportimport.site.LARSiteReader;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Petteri Karttunen
 */
public class SiteImporterTest {

	@ClassRule
	@Rule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		_groupLocalService = Mockito.mock(GroupLocalService.class);
		_siteReporter = Mockito.mock(SiteReporter.class);

		_siteImporter = new SiteImporter(
			Mockito.mock(ExportImportSiteProvider.class), _groupLocalService,
			Mockito.mock(LARSiteReader.class),
			Mockito.mock(PortletDataContextFactory.class), _siteReporter);

		_portletDataContext = Mockito.mock(PortletDataContext.class);

		Mockito.when(
			_portletDataContext.getCompanyId()
		).thenReturn(
			_COMPANY_ID
		);
	}

	@Test
	public void testSortAncestorsFirst() {

		// The manifest lists the sites in the order they were selected for
		// export, so a site can be listed before the site it sat below

		LARSite grandParentLARSite = _createLARSite("grandparent", null);
		LARSite parentLARSite = _createLARSite("parent", "grandparent");
		LARSite childLARSite = _createLARSite("child", "parent");

		List<LARSite> larSites = _sortAncestorsFirst(
			Arrays.asList(childLARSite, grandParentLARSite, parentLARSite));

		Assert.assertEquals(
			Arrays.asList(grandParentLARSite, parentLARSite, childLARSite),
			larSites);
	}

	@Test
	public void testSortAncestorsFirstWhenParentIsNotCarried() {

		// A site whose parent is not arriving sits as deep as a top level site,
		// because there is nothing for it to be put below

		LARSite larSite1 = _createLARSite("site1", "elsewhere");
		LARSite larSite2 = _createLARSite("site2", null);

		Assert.assertEquals(
			Arrays.asList(larSite1, larSite2),
			_sortAncestorsFirst(Arrays.asList(larSite1, larSite2)));
	}

	@Test
	public void testSortAncestorsFirstWhenSitesSitBelowEachOther() {
		LARSite larSite1 = _createLARSite("site1", "site2");
		LARSite larSite2 = _createLARSite("site2", "site1");

		// Counting how deep either site sat has to end rather than go around
		// forever

		List<LARSite> larSites = _sortAncestorsFirst(
			Arrays.asList(larSite1, larSite2));

		Assert.assertEquals(larSites.toString(), 2, larSites.size());
	}

	@Test
	public void testUpdateParentSiteWhenParentSiteExternalReferenceCodeIsNull()
		throws Exception {

		_updateParentSite(_createLARSite("child", null), _mockSite(0));

		Mockito.verifyNoInteractions(_groupLocalService);
		Mockito.verifyNoInteractions(_siteReporter);
	}

	@Test
	public void testUpdateParentSiteWhenParentSiteIsAlreadyTheParent()
		throws Exception {

		Group parentGroup = _mockSite(0);

		Mockito.when(
			parentGroup.getGroupId()
		).thenReturn(
			_PARENT_GROUP_ID
		);

		Mockito.when(
			_groupLocalService.fetchGroupByExternalReferenceCode(
				"parent", _COMPANY_ID)
		).thenReturn(
			parentGroup
		);

		_updateParentSite(
			_createLARSite("child", "parent"), _mockSite(_PARENT_GROUP_ID));

		_verifyNoSiteWasMoved();

		Mockito.verifyNoInteractions(_siteReporter);
	}

	@Test
	public void testUpdateParentSiteWhenParentSiteIsMissing() throws Exception {
		LARSite larSite = _createLARSite("child", "parent");

		_updateParentSite(larSite, _mockSite(0));

		// A parent that exists nowhere is the user not getting the site where
		// they asked for it, which is worth telling them about

		Mockito.verify(
			_siteReporter
		).reportMissingParentSite(
			_portletDataContext, larSite
		);

		_verifyNoSiteWasMoved();
	}

	@Test
	public void testUpdateParentSiteWhenParentSiteIsNotYetTheParent()
		throws Exception {

		Group parentGroup = _mockSite(0);

		Mockito.when(
			parentGroup.getGroupId()
		).thenReturn(
			_PARENT_GROUP_ID
		);

		Mockito.when(
			_groupLocalService.fetchGroupByExternalReferenceCode(
				"parent", _COMPANY_ID)
		).thenReturn(
			parentGroup
		);

		Group group = _mockSite(0);

		_updateParentSite(_createLARSite("child", "parent"), group);

		// The site keeps everything but the parent, and no service context
		// takes part, because only where the site sits is being changed

		Mockito.verify(
			_groupLocalService
		).updateGroup(
			group.getGroupId(), _PARENT_GROUP_ID, group.getNameMap(),
			group.getDescriptionMap(), group.getType(), group.getTypeSettings(),
			group.isManualMembership(), group.getMembershipRestriction(),
			group.getFriendlyURL(), group.isInheritContent(), group.isActive(),
			null
		);

		Mockito.verifyNoInteractions(_siteReporter);
	}

	private LARSite _createLARSite(
		String externalReferenceCode, String parentExternalReferenceCode) {

		return new LARSite(
			0, externalReferenceCode, _GROUP_ID, "Global", "Site",
			parentExternalReferenceCode, false);
	}

	private Group _mockSite(long parentGroupId) {
		Group group = Mockito.mock(Group.class);

		Mockito.when(
			group.getGroupId()
		).thenReturn(
			_GROUP_ID
		);

		Mockito.when(
			group.getParentGroupId()
		).thenReturn(
			parentGroupId
		);

		return group;
	}

	private List<LARSite> _sortAncestorsFirst(List<LARSite> larSites) {
		return ReflectionTestUtil.invoke(
			_siteImporter, "_sortAncestorsFirst", new Class<?>[] {List.class},
			ListUtil.copy(larSites));
	}

	private void _updateParentSite(LARSite larSite, Group group) {
		ReflectionTestUtil.invoke(
			_siteImporter, "_updateParentSite",
			new Class<?>[] {
				PortletDataContext.class, LARSite.class, Group.class
			},
			_portletDataContext, larSite, group);
	}

	private void _verifyNoSiteWasMoved() throws Exception {
		Mockito.verify(
			_groupLocalService, Mockito.never()
		).updateGroup(
			Mockito.anyLong(), Mockito.anyLong(), Mockito.any(), Mockito.any(),
			Mockito.anyInt(), Mockito.any(), Mockito.anyBoolean(),
			Mockito.anyInt(), Mockito.any(), Mockito.anyBoolean(),
			Mockito.anyBoolean(), Mockito.any()
		);
	}

	private static final long _COMPANY_ID = 1;

	private static final long _GROUP_ID = 2;

	private static final long _PARENT_GROUP_ID = 3;

	private GroupLocalService _groupLocalService;
	private PortletDataContext _portletDataContext;
	private SiteImporter _siteImporter;
	private SiteReporter _siteReporter;

}