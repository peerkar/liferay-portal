/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.configuration;

import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.search.opensearch2.internal.connection.OpenSearchConnectionFixture;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Map;
import java.util.Properties;

import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class OpenSearchConfigurationTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testConfigurationsFromBuildTestXmlAntFile() throws Exception {
		Map<String, Object> configurationProperties =
			_loadConfigurationProperties(
				"OpenSearchConfigurationTest-build-test-xml.cfg");

		OpenSearchConnectionFixture openSearchConnectionFixture =
			OpenSearchConnectionFixture.builder(
			).clusterName(
				OpenSearchConfigurationTest.class.getSimpleName()
			).elasticsearchConfigurationProperties(
				configurationProperties
			).build();

		try {
			openSearchConnectionFixture.createNode();
		}
		finally {
			openSearchConnectionFixture.destroyNode();
		}
	}

	private Map<String, Object> _loadConfigurationProperties(String fileName)
		throws Exception {

		Properties properties = new Properties();

		Class<?> clazz = getClass();

		properties.load(clazz.getResourceAsStream(fileName));

		return PropertiesUtil.toMap(properties);
	}

}