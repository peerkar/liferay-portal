/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.application.list.internal.search;

import com.liferay.application.list.PanelApp;
import com.liferay.application.list.PanelAppRegistry;
import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.constants.PanelAppFieldNames;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.application.list.display.context.logic.PanelCategoryHelper;
import com.liferay.application.list.util.PanelCategoryRegistryUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.PortletCategoryKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.index.IndexStatusManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = {"index.on.startup=false", "system.index=true"},
	service = Indexer.class
)
public class PanelAppIndexer extends BaseIndexer<PanelApp> {

	@Override
	public String getClassName() {
		return PanelApp.class.getName();
	}

	@Override
	public BooleanQuery getFullQuery(SearchContext searchContext)
		throws SearchException {

		try {
			BooleanFilter fullQueryBooleanFilter = new BooleanFilter();

			fullQueryBooleanFilter.addRequiredTerm(
				Field.ENTRY_CLASS_NAME, getClassName());

			BooleanQuery fullQuery = createFullQuery(
				fullQueryBooleanFilter, searchContext);

			fullQuery.setQueryConfig(searchContext.getQueryConfig());

			return fullQuery;
		}
		catch (SearchException searchException) {
			throw searchException;
		}
		catch (Exception exception) {
			throw new SearchException(exception);
		}
	}

	@Override
	public void reindex(Collection<PanelApp> panelApps) {
		if (_indexStatusManager.isIndexReadOnly() ||
			_indexStatusManager.isIndexReadOnly(getClassName()) ||
			!isIndexerEnabled() || panelApps.isEmpty()) {

			return;
		}

		List<Document> documents = new ArrayList<>();

		try {
			for (PanelApp panelApp : panelApps) {
				if (panelApp == null) {
					return;
				}

				documents.add(getDocument(panelApp));
			}

			_indexWriterHelper.updateDocuments(
				CompanyConstants.SYSTEM, documents, false);
		}
		catch (SearchException searchException) {
			_log.error(searchException);
		}
	}

	@Override
	public Hits search(SearchContext searchContext) throws SearchException {
		try {
			Hits hits = doSearch(searchContext);

			processHits(searchContext, hits);

			return hits;
		}
		catch (SearchException searchException) {
			throw searchException;
		}
		catch (Exception exception) {
			throw new SearchException(exception);
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		setCommitImmediately(false);
		setDefaultSelectedFieldNames(
			Field.COMPANY_ID, Field.DESCRIPTION, Field.ENTRY_CLASS_NAME,
			Field.TITLE, Field.UID, PanelAppFieldNames.PANEL_APP_CATEGORY,
			PanelAppFieldNames.PANEL_APP_CATEGORY_KEY);

		setFilterSearch(true);
		setPermissionAware(true);
		setSelectAllLocales(false);
		setStagingAware(false);

		_bundleContext = bundleContext;

		_panelCategoryHelper = new PanelCategoryHelper(_panelAppRegistry);
	}

	@Override
	protected BooleanQuery createFullQuery(
			BooleanFilter fullQueryBooleanFilter, SearchContext searchContext)
		throws Exception {

		BooleanQuery searchQuery = new BooleanQueryImpl();

		addSearchLocalizedTerm(
			searchQuery, searchContext, Field.DESCRIPTION, false);
		addSearchLocalizedTerm(searchQuery, searchContext, Field.TITLE, false);
		addSearchLocalizedTerm(
			searchQuery, searchContext, PanelAppFieldNames.PANEL_APP_CATEGORY,
			false);
		addSearchTerm(
			searchQuery, searchContext, PanelAppFieldNames.PANEL_APP_KEY,
			false);

		BooleanQuery fullBooleanQuery = new BooleanQueryImpl();

		if (fullQueryBooleanFilter.hasClauses()) {
			fullBooleanQuery.setPreBooleanFilter(fullQueryBooleanFilter);
		}

		fullBooleanQuery.add(searchQuery, BooleanClauseOccur.MUST);

		return fullBooleanQuery;
	}

	@Override
	protected void doDelete(PanelApp panelApp) throws Exception {
		_indexWriterHelper.deleteDocument(
			CompanyConstants.SYSTEM, _getUID(panelApp), isCommitImmediately());
	}

	@Override
	protected Document doGetDocument(PanelApp panelApp) throws Exception {
		Document document = newDocument();

		Portlet portlet = panelApp.getPortlet();

		document.addUID(panelApp.getPortletId(), panelApp.getKey());

		document.addKeyword(Field.COMPANY_ID, CompanyConstants.SYSTEM);
		document.addKeyword(Field.ENTRY_CLASS_NAME, getClassName());
		document.addKeyword(
			PanelAppFieldNames.PANEL_APP_CATEGORY_KEY,
			portlet.getControlPanelEntryCategory());
		document.addKeyword(
			PanelAppFieldNames.PANEL_APP_KEY, panelApp.getKey());
		document.addKeyword(
			PanelAppFieldNames.PANEL_APP_ROOT_CATEGORY,
			_getPanelAppRootCategory(portlet));
		document.addKeyword(PanelAppFieldNames.PORTLET_ICON, portlet.getIcon());
		document.addKeyword(
			PanelAppFieldNames.PORTLET_ID, portlet.getPortletId());

		for (Locale locale : _language.getAvailableLocales()) {
			String languageId = LocaleUtil.toLanguageId(locale);

			document.addText(
				_localization.getLocalizedName(
					PanelAppFieldNames.PANEL_APP_CATEGORY, languageId),
				panelApp.getLabel(locale));

			document.addText(
				_localization.getLocalizedName(Field.DESCRIPTION, languageId),
				_language.get(
					locale,
					"javax.portlet.description." + portlet.getPortletId(),
					StringPool.BLANK));

			document.addText(
				_localization.getLocalizedName(Field.TITLE, languageId),
				_getTitle(locale, portlet));
		}

		return document;
	}

	@Override
	protected Summary doGetSummary(
			Document document, Locale locale, String snippet,
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		Summary summary = createSummary(
			document, Field.TITLE, Field.DESCRIPTION);

		summary.setMaxContentLength(200);

		return summary;
	}

	@Override
	protected void doReindex(PanelApp panelApp) throws Exception {
		_indexWriterHelper.updateDocument(
			CompanyConstants.SYSTEM, getDocument(panelApp));
	}

	@Override
	protected void doReindex(String className, long classPK) throws Exception {
	}

	@Override
	protected void doReindex(String[] ids) throws Exception {
		String[] panelCategories = {
			PanelCategoryKeys.APPLICATIONS_MENU, PanelCategoryKeys.COMMERCE,
			PanelCategoryKeys.CONTROL_PANEL, PanelCategoryKeys.USER_MY_ACCOUNT,
			PanelCategoryKeys.SITE_ADMINISTRATION
		};

		for (String panelCategory : panelCategories) {
			List<PanelApp> panelApps = _panelCategoryHelper.getAllPanelApps(
				panelCategory);

			for (PanelApp panelApp : panelApps) {
				doReindex(panelApp);
			}
		}
	}

	private String _getPanelAppRootCategory(Portlet portlet) {
		if (Objects.equals(
				portlet.getControlPanelEntryCategory(),
				PanelCategoryKeys.USER_MY_ACCOUNT)) {

			return PortletCategoryKeys.USER_MY_ACCOUNT;
		}

		String panelCategoryKey = _getPanelCategoryKey(
			portlet.getControlPanelEntryCategory());

		if (Validator.isBlank(panelCategoryKey)) {
			return null;
		}

		if (panelCategoryKey.startsWith(PanelCategoryKeys.APPLICATIONS_MENU)) {
			return PanelCategoryKeys.APPLICATIONS_MENU;
		}
		else if (panelCategoryKey.startsWith(PanelCategoryKeys.COMMERCE)) {
			return PanelCategoryKeys.COMMERCE;
		}
		else if (panelCategoryKey.startsWith(PanelCategoryKeys.CONTROL_PANEL)) {
			return PanelCategoryKeys.CONTROL_PANEL;
		}
		else if (panelCategoryKey.startsWith(
					PanelCategoryKeys.SITE_ADMINISTRATION)) {

			return PanelCategoryKeys.SITE_ADMINISTRATION;
		}

		return null;
	}

	private String _getPanelCategoryKey(String controlPanelEntryCategory) {
		try {
			PanelCategory panelCategory =
				PanelCategoryRegistryUtil.getPanelCategory(
					controlPanelEntryCategory);

			Collection<ServiceReference<PanelCategory>> serviceReferences =
				_bundleContext.getServiceReferences(PanelCategory.class, null);

			Iterator<ServiceReference<PanelCategory>> iterator =
				serviceReferences.iterator();

			while (iterator.hasNext()) {
				ServiceReference<PanelCategory> panelCategoryServiceReference =
					iterator.next();

				if (Objects.equals(
						(String)panelCategoryServiceReference.getProperty(
							"component.name"),
						panelCategory.getClass(
						).getName())) {

					return (String)panelCategoryServiceReference.getProperty(
						"panel.category.key");
				}
			}
		}
		catch (IllegalArgumentException illegalArgumentException) {
			if (_log.isWarnEnabled()) {
				_log.warn(illegalArgumentException);
			}
		}
		catch (InvalidSyntaxException invalidSyntaxException) {
			if (_log.isDebugEnabled()) {
				_log.debug(invalidSyntaxException);
			}
		}

		return null;
	}

	private String _getTitle(Locale locale, Portlet portlet) {
		String languageKey =
			JavaConstants.JAVAX_PORTLET_TITLE + StringPool.PERIOD +
				portlet.getPortletId();

		String localization = _language.get(locale, languageKey);

		if (!Objects.equals(languageKey, localization)) {
			return localization;
		}

		return portlet.getDisplayName();
	}

	private String _getUID(PanelApp panelApp) {
		return Field.getUID(panelApp.getPortletId(), panelApp.getKey());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PanelAppIndexer.class);

	private BundleContext _bundleContext;

	@Reference
	private IndexStatusManager _indexStatusManager;

	@Reference
	private IndexWriterHelper _indexWriterHelper;

	@Reference
	private Language _language;

	@Reference
	private Localization _localization;

	@Reference
	private PanelAppRegistry _panelAppRegistry;

	private PanelCategoryHelper _panelCategoryHelper;

}