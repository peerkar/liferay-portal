package com.liferay.portal.search.tuning.gsearch.wikipedia.data.importer.internal.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.search.tuning.gsearch.wikipedia.data.importer.internal.constants.WikiPediaDataImportPortletKeys;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.gsearch",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=wikipediaimport-portlet",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + WikiPediaDataImportPortletKeys.WIKIPEDIA_DATA_IMPORT,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class WikiPediaDataImportPortlet extends MVCPortlet {
}