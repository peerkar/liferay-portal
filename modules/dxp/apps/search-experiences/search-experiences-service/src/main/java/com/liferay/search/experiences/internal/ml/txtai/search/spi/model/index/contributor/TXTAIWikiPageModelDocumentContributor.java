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

package com.liferay.search.experiences.internal.ml.txtai.search.spi.model.index.contributor;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.search.experiences.internal.configuration.TXTAIConfiguration;
import com.liferay.wiki.model.WikiPage;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	configurationPid = "com.liferay.search.experiences.internal.configuration.TXTAIConfiguration",
	enabled = false, immediate = true,
	property = "indexer.class.name=com.liferay.wiki.model.WikiPage",
	service = ModelDocumentContributor.class
)
public class TXTAIWikiPageModelDocumentContributor
	extends BaseTXTAIModelDocumentContributor
	implements ModelDocumentContributor<WikiPage> {

	@Override
	public void contribute(Document document, WikiPage wikiPage) {
		if (!isAddTextEmbeddings(WikiPage.class, _txtAIConfiguration)) {
			return;
		}

		addTextEmbedding(document, wikiPage.getContent(), _txtAIConfiguration);
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_txtAIConfiguration = ConfigurableUtil.createConfigurable(
			TXTAIConfiguration.class, properties);
	}

	private volatile TXTAIConfiguration _txtAIConfiguration;

}