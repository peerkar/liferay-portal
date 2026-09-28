/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mcp.server.rest.internal.search.index;

import com.liferay.mcp.server.rest.internal.search.index.constants.MCPToolConstants;
import com.liferay.mcp.server.rest.internal.search.index.util.IntentUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Petteri Karttunen
 */
public class MCPToolFactoryUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testGetExpansions() {
		Assert.assertArrayEquals(
			new String[] {
				"add blog posting in a site", "create blog posting in a site",
				"make blog posting in a site", "write blog posting in a site"
			},
			_getExpansions(
				false, false, "blog posting", IntentUtil.INTENT_CREATE, null,
				"/sites/{siteId}/blog-postings", "postSiteBlogPosting"));
	}

	@Test
	public void testGetExpansionsWithABatchTool() {
		String[] expansions = _getExpansions(
			true, false, "blog posting", IntentUtil.INTENT_CREATE, null,
			"/sites/{siteId}/blog-postings/batch", "postSiteBlogPostingBatch");

		Assert.assertEquals("batch add blog postings in a site", expansions[0]);

		for (String expansion : expansions) {
			Assert.assertTrue(
				expansion, StringUtil.startsWith(expansion, "batch "));
		}
	}

	@Test
	public void testGetExpansionsWithACollectionNounAfterTheEntity() {
		String[] expansions = _getExpansions(
			false, true, "catalog", IntentUtil.INTENT_LIST, null,
			"/catalog/{catalogId}/permissions", "getCatalogPermissionsPage");

		Assert.assertEquals(
			"browse catalog permissions by catalog id", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithACollectionTool() {
		Assert.assertArrayEquals(
			new String[] {
				"browse blog postings in a site",
				"find blog postings in a site",
				"get all blog postings in a site",
				"list blog postings in a site", "see blog postings in a site",
				"show blog postings in a site"
			},
			_getExpansions(
				false, true, "blog posting", IntentUtil.INTENT_LIST, null,
				"/sites/{siteId}/blog-postings", "getSiteBlogPostingsPage"));
	}

	@Test
	public void testGetExpansionsWithACountableWordEndingInS() {
		String[] expansions = _getExpansions(
			false, true, "process metric", IntentUtil.INTENT_LIST, null,
			"/processes/{processId}/metrics", "getProcessMetricsPage");

		Assert.assertTrue(
			expansions[0],
			StringUtil.startsWith(expansions[0], "browse process metrics"));
	}

	@Test
	public void testGetExpansionsWithAModifierAfterTheEntity() {
		String[] expansions = _getExpansions(
			false, true, "document", IntentUtil.INTENT_LIST, null,
			"/sites/{siteId}/documents/rated-by-me",
			"getSiteDocumentsRatedByMePage");

		Assert.assertEquals(
			"browse documents in a site rated by me", expansions[0]);

		expansions = _getExpansions(
			false, true, "warehouse item", IntentUtil.INTENT_LIST, null,
			"/warehouseItems/updated", "getWarehouseItemsUpdatedPage");

		Assert.assertEquals("browse warehouse items updated", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAParameterBetweenTheEntityWords() {
		String[] expansions = _getExpansions(
			false, true, "option value", IntentUtil.INTENT_LIST, null,
			"/options/{id}/optionValues", "getOptionIdOptionValuesPage");

		Assert.assertEquals("browse option values by id", expansions[0]);

		expansions = _getExpansions(
			false, true, "related product", IntentUtil.INTENT_LIST, null,
			"/products/{id}/relatedProducts",
			"getProductIdRelatedProductsPage");

		Assert.assertEquals("browse related products by id", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAParameterWordInsideAnotherWord() {
		String[] expansions = _getExpansions(
			false, false, "provider", IntentUtil.INTENT_READ, null,
			"/providers/{id}", "getProvider");

		Assert.assertEquals("fetch provider by id", expansions[0]);

		expansions = _getExpansions(
			false, false, "comment", IntentUtil.INTENT_READ, null,
			"/videos/{videoId}/comments/{id}", "getVideoComment");

		Assert.assertEquals("fetch comment in a video by id", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAParentReachedByExternalReferenceCode() {
		String[] expansions = _getExpansions(
			false, true, "blog posting", IntentUtil.INTENT_LIST, null,
			"/sites/by-external-reference-code/{siteExternalReferenceCode}" +
				"/blog-postings",
			"getSiteBlogPostingsPage");

		Assert.assertEquals("browse blog postings in a site", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAParentStartingWithAVowel() {
		String[] expansions = _getExpansions(
			false, true, "document", IntentUtil.INTENT_LIST, null,
			"/asset-libraries/{assetLibraryId}/documents",
			"getAssetLibraryDocumentsPage");

		Assert.assertEquals(
			"browse documents in an asset library", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAParentThatIsNotASite() {
		String[] expansions = _getExpansions(
			false, true, "assignable user", IntentUtil.INTENT_LIST, null,
			"/workflow-tasks/{workflowTaskId}/assignable-users",
			"getWorkflowTaskAssignableUsersPage");

		Assert.assertEquals(
			"browse assignable users in a workflow task", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAParentThatIsPartOfTheEntity() {
		String[] expansions = _getExpansions(
			false, true, "process metric", IntentUtil.INTENT_LIST, null,
			"/processes/{processId}/metrics", "getProcessMetricsPage");

		Assert.assertEquals(
			"browse process metrics by process id", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAPathParameterInTheToolName() {
		String[] expansions = _getExpansions(
			false, false, "blog posting", IntentUtil.INTENT_READ, null,
			"/sites/{siteId}/blog-postings/by-external-reference-code" +
				"/{externalReferenceCode}",
			"getSiteBlogPostingByExternalReferenceCode");

		Assert.assertEquals(
			"fetch blog posting in a site by external reference code",
			expansions[0]);
	}

	@Test
	public void testGetExpansionsWithATagAbsentFromTheToolName() {
		String[] expansions = _getExpansions(
			false, true, "assignee", IntentUtil.INTENT_LIST, null,
			"/workflow-tasks/{workflowTaskId}/assignable-users",
			"getWorkflowTaskAssignableUsersPage");

		Assert.assertEquals(
			"browse assignable users in a workflow task", expansions[0]);

		expansions = _getExpansions(
			false, false, "organization", "delete", null,
			"/organizations/{organizationId}/user-accounts/by-email-address" +
				"/{emailAddress}",
			"deleteUserAccountByEmailAddress");

		Assert.assertEquals(
			"delete user account by email address", expansions[0]);

		expansions = _getExpansions(
			false, false, "object entry", IntentUtil.INTENT_READ, null,
			"/by-external-reference-code/{externalReferenceCode}",
			"getByExternalReferenceCode");

		Assert.assertEquals(
			"fetch object entry by external reference code", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAnEntityEndingInPage() {
		String[] expansions = _getExpansions(
			false, false, "wiki page", IntentUtil.INTENT_READ, null,
			"/wiki-pages/{wikiPageId}", "getWikiPage");

		Assert.assertEquals("fetch wiki page by wiki page id", expansions[0]);

		expansions = _getExpansions(
			false, false, "wiki page", IntentUtil.INTENT_CREATE, null,
			"/wiki-nodes/{wikiNodeId}/wiki-pages", "postWikiNodeWikiPage");

		Assert.assertEquals("add wiki page in a wiki node", expansions[0]);
	}

	@Test
	public void testGetExpansionsWithAnEntityEndingInSS() {
		String[] expansions = _getExpansions(
			false, true, "export process", IntentUtil.INTENT_LIST, null,
			"/sites/{siteId}/export-processes", "getSiteExportProcessesPage");

		Assert.assertTrue(
			expansions[0],
			StringUtil.startsWith(expansions[0], "browse export processes "));
	}

	@Test
	public void testGetExpansionsWithAnOperationActionSuffix() {
		Assert.assertArrayEquals(
			new String[] {
				"follow structured content by structured content id",
				"subscribe structured content by structured content id",
				"watch structured content by structured content id"
			},
			_getExpansions(
				false, false, "structured content", "subscribe", "Subscribe",
				"/structured-contents/{structuredContentId}/subscribe",
				"putStructuredContentSubscribe"));
	}

	@Test
	public void testGetExpansionsWithAnOperationActionSuffixInAParent() {
		Assert.assertArrayEquals(
			new String[] {
				"follow blog postings in a site",
				"subscribe blog postings in a site",
				"watch blog postings in a site"
			},
			_getExpansions(
				false, false, "blog posting", "subscribe", "Subscribe",
				"/sites/{siteId}/blog-postings/subscribe",
				"putSiteBlogPostingSubscribe"));
	}

	@Test
	public void testGetExpansionsWithTheEntityAsTheParent() {
		String[] expansions = _getExpansions(
			false, false, "site", IntentUtil.INTENT_READ, null,
			"/sites/by-external-reference-code/{externalReferenceCode}",
			"getSiteByExternalReferenceCode");

		Assert.assertEquals(
			"fetch site by external reference code", expansions[0]);

		for (String expansion : expansions) {
			Assert.assertFalse(expansion, expansion.contains("in a site"));
		}
	}

	@Test
	public void testGetExpansionsWithoutAnEntity() {
		Assert.assertEquals(
			0,
			_getExpansions(
				false, false, StringPool.BLANK, IntentUtil.INTENT_CREATE, null,
				"/sites", "postSite").length);
		Assert.assertEquals(
			0,
			_getExpansions(
				false, false, "site", StringPool.BLANK, null, "/sites",
				"headSite").length);
	}

	@Test
	public void testGetOperationVariant() {
		Assert.assertEquals(
			StringPool.BLANK,
			_getOperationVariant(
				"BlogPosting", "/sites/{siteId}/blog-postings"));
		Assert.assertEquals(
			StringPool.BLANK,
			_getOperationVariant("CartItem", "/carts/{cartId}/items"));
		Assert.assertEquals(
			StringPool.BLANK,
			_getOperationVariant(
				"ProcessMetric", "/processes/{processId}/metrics"));
		Assert.assertEquals(
			MCPToolConstants.OPERATION_VARIANT_TRAVERSAL,
			_getOperationVariant(
				"Assignee",
				"/workflow-tasks/{workflowTaskId}/assignable-users"));
		Assert.assertEquals(
			MCPToolConstants.OPERATION_VARIANT_TRAVERSAL,
			_getOperationVariant("Attachment", "/products/{id}/images"));
	}

	@Test
	public void testGetReferenceCollectionSegment() {
		Assert.assertEquals(
			"content-structures",
			_getReferenceCollectionSegment("contentStructureId"));
		Assert.assertEquals("sites", _getReferenceCollectionSegment("siteId"));
		Assert.assertEquals(
			"taxonomy-categories",
			_getReferenceCollectionSegment("taxonomyCategoryIds"));
		Assert.assertNull(_getReferenceCollectionSegment("id"));
		Assert.assertNull(_getReferenceCollectionSegment("title"));
		Assert.assertNull(_getReferenceCollectionSegment("type"));
	}

	private String[] _getExpansions(
		boolean batch, boolean collection, String entityWords, String intent,
		String operationActionSuffix, String path, String toolName) {

		return ReflectionTestUtil.invoke(
			MCPToolFactoryUtil.class, "_getExpansions",
			new Class<?>[] {
				boolean.class, boolean.class, String.class, String.class,
				String.class, String.class, String.class
			},
			batch, collection, entityWords, intent, operationActionSuffix, path,
			toolName);
	}

	private String _getOperationVariant(String entityName, String path) {
		return ReflectionTestUtil.invoke(
			MCPToolFactoryUtil.class, "_getOperationVariant",
			new Class<?>[] {String.class, String.class}, entityName, path);
	}

	private String _getReferenceCollectionSegment(String propertyName) {
		return ReflectionTestUtil.invoke(
			MCPToolFactoryUtil.class, "_getReferenceCollectionSegment",
			new Class<?>[] {String.class}, propertyName);
	}

}