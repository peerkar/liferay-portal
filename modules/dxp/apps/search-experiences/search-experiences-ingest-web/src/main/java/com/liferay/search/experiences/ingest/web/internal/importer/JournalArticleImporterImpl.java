/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.ingest.web.internal.importer;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PwdGenerator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;

import java.text.BreakIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;

/**
 * @author Petteri Karttunen
 */
public class JournalArticleImporterImpl implements JournalArticleImporter {

	public JournalArticleImporterImpl(
		List<Long> groupIds,
		JournalArticleLocalService journalArticleLocalService,
		String languageId, PortletRequest portletRequest, List<Long> userIds) {

		_groupIds = groupIds;
		_journalArticleLocalService = journalArticleLocalService;
		_languageId = languageId;
		_portletRequest = portletRequest;
		_userIds = userIds;
	}

	@Override
	public JournalArticle addJournalArticle(
		String[] assetTagNames, String content, String title) {

		if (_log.isInfoEnabled()) {
			_log.info("Add journal article " + title);
		}

		Locale locale = LocaleUtil.fromLanguageId(_languageId);

		try {
			JournalArticle journalArticle =
				_journalArticleLocalService.addArticle(
					_nextUserId(), _nextGroupId(), 0,
					HashMapBuilder.put(
						locale, title
					).build(),
					HashMapBuilder.put(
						locale, _createDescription(content)
					).build(),
					_createArticleXML(content, _languageId),
					"BASIC-WEB-CONTENT", "BASIC-WEB-CONTENT",
					_getServiceContext(assetTagNames, _portletRequest));

			_ingestedTitles.add(title);

			return journalArticle;
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			_failedTitles.add(title);
		}

		return null;
	}

	@Override
	public List<String> getFailedTitles() {
		return _failedTitles;
	}

	@Override
	public List<String> getIngestedTitles() {
		return _ingestedTitles;
	}

	@Override
	public Map<String, List<String>> getIngestResults() {
		return HashMapBuilder.<String, List<String>>put(
			"failedItems", _failedTitles
		).put(
			"ingestedItems", _ingestedTitles
		).build();
	}

	@Override
	public int getNumberOfProcessedItems() {
		return _failedTitles.size() + _ingestedTitles.size();
	}

	@Override
	public JournalArticle updateJournalArticle(JournalArticle journalArticle) {
		return _journalArticleLocalService.updateJournalArticle(journalArticle);
	}

	private String _createArticleXML(String content, String languageId) {
		StringBundler sb = new StringBundler(13);

		sb.append("<root available-locales=\"en_US\" default-locale=\"");
		sb.append(languageId);
		sb.append("\">");
		sb.append("<dynamic-element name=\"content\" type=\"text_area\" ");
		sb.append("index-type=\"text\" instance-id=\"");
		sb.append(_generateInstanceId());
		sb.append("\">");
		sb.append("<dynamic-content language-id=\"");
		sb.append(languageId);
		sb.append("\"><![CDATA[");
		sb.append(content);
		sb.append("]]></dynamic-content></dynamic-element>");
		sb.append("</root>");

		return sb.toString();
	}

	private String _createDescription(String s) {
		s = HtmlUtil.stripHtml(s);

		if (Validator.isBlank(s)) {
			return StringPool.BLANK;
		}

		BreakIterator breakIterator = BreakIterator.getSentenceInstance();

		breakIterator.setText(s);

		if (s.length() > _DESCRIPTION_MAX_LENGTH) {
			return s.substring(0, breakIterator.preceding(500));
		}

		return s;
	}

	private String _generateInstanceId() {
		StringBuilder instanceId = new StringBuilder(8);

		String key = PwdGenerator.KEY1 + PwdGenerator.KEY2 + PwdGenerator.KEY3;

		for (int i = 0; i < 8; i++) {
			int pos = (int)Math.floor(Math.random() * key.length());

			instanceId.append(key.charAt(pos));
		}

		return instanceId.toString();
	}

	private ServiceContext _getServiceContext(
			String[] assetTagNames, PortletRequest portletRequest)
		throws PortalException {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalArticle.class.getName(), portletRequest);

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setAssetTagNames(assetTagNames);

		return serviceContext;
	}

	private long _nextGroupId() {
		if (_groupIdx == _groupIds.size()) {
			_groupIdx = 0;
		}

		return _groupIds.get(_groupIdx++);
	}

	private long _nextUserId() {
		if (_userIdx == _userIds.size()) {
			_userIdx = 0;
		}

		return _userIds.get(_userIdx++);
	}

	private static final int _DESCRIPTION_MAX_LENGTH = 500;

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleImporterImpl.class);

	private final List<String> _failedTitles = new ArrayList<>();
	private final List<Long> _groupIds;
	private int _groupIdx;
	private final List<String> _ingestedTitles = new ArrayList<>();
	private final JournalArticleLocalService _journalArticleLocalService;
	private final String _languageId;
	private final PortletRequest _portletRequest;
	private final List<Long> _userIds;
	private int _userIdx;

}