
package com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.facet.handler;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.search.aggregation.AggregationResult;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.TermsAggregationResult;
import com.liferay.portal.search.tuning.gsearch.constants.JSONResponseKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.facet.FacetHandler;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=ddm_structure_name",
	service = FacetHandler.class
)
public class DDMStructureNameFacetHandler
	extends BaseFacetHandler implements FacetHandler {

	@Override
	public Optional<JSONObject> getResultsObject(
		SearchRequestContext queryContext, AggregationResult aggregationResult,
		JSONObject configurationJsonObject) {

		TermsAggregationResult termsAggregationResult =
			(TermsAggregationResult)aggregationResult;

		Locale locale = queryContext.getLocale();

		JSONArray termsArray = JSONFactoryUtil.createJSONArray();

		for (Bucket bucket : termsAggregationResult.getBuckets()) {
			try {
				JSONObject jsonObject = _getDDMStructureObject(bucket, locale);

				termsArray.put(jsonObject);
			}
			catch (PortalException portalException) {
				queryContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.ddm-structure-not-found",
						portalException.getMessage(), portalException,
						configurationJsonObject, null, null));

				if (_log.isWarnEnabled()) {
					_log.warn(portalException.getMessage(), portalException);
				}
			}
		}

		return createResultObject(termsArray, configurationJsonObject);
	}

	private DDMStructure _getDDMStructure(String ddmStructureKey)
		throws PortalException {

		DynamicQuery structureQuery = _ddmStructureLocalService.dynamicQuery();

		structureQuery.add(
			RestrictionsFactoryUtil.eq("structureKey", ddmStructureKey));

		List<DDMStructure> structures =
			DDMStructureLocalServiceUtil.dynamicQuery(structureQuery);

		return structures.get(0);
	}

	private JSONObject _getDDMStructureObject(Bucket bucket, Locale locale)
		throws PortalException {

		DDMStructure structure = _getDDMStructure(bucket.getKey());

		JSONObject item = JSONFactoryUtil.createJSONObject();

		item.put(JSONResponseKeys.FREQUENCY, bucket.getDocCount());
		item.put(
			JSONResponseKeys.GROUP_NAME,
			_groupLocalService.getGroup(
				structure.getGroupId()
			).getName(
				locale, true
			));
		item.put(JSONResponseKeys.NAME, structure.getName(locale, true));
		item.put(JSONResponseKeys.VALUE, bucket.getKey());

		return item;
	}

	private static final Logger _log = LoggerFactory.getLogger(
		DDMStructureNameFacetHandler.class);

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

}