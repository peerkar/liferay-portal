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

package com.liferay.portal.search.tuning.gsearch.impl.internal.results.item.builder;

import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.results.ResultAttributes;
import com.liferay.portal.search.tuning.gsearch.spi.results.item.ResultItemBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.document.library.kernel.model.DLFileEntry",
	service = ResultItemBuilder.class
)
public class DLFileEntryItemBuilder
	extends BaseResultItemBuilder implements ResultItemBuilder {

	@Override
	public Map<String, String> getMetadata(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception {

		Map<String, String> metaData = new HashMap<>();

		String mimeType = document.getString("mimeType");

		metaData.put("format", _translateMimetype(mimeType));
		metaData.put("size", _getSize(document));

		if (mimeType.startsWith("image_")) {
			_setImageMetadata(searchRequestContext, document, metaData);
		}

		return metaData;
	}

	@Override
	public String getThumbnail(
			SearchRequestContext searchRequestContext,
			ResultAttributes resultAttributes, Document document)
		throws Exception {

		StringBundler sb = new StringBundler(7);

		sb.append("/documents/");
		sb.append(document.getString(Field.SCOPE_GROUP_ID));
		sb.append("/");
		sb.append(document.getString(Field.FOLDER_ID));
		sb.append("/");
		sb.append(document.getString("path"));
		sb.append("?imageThumbnail=1");

		return sb.toString();
	}

	protected static final long KBYTES = 1024;

	protected static final long MBYTES = 1024 * 1024;

	private String _getSize(Document document) {
		long size = document.getLong("size");

		StringBundler sb = new StringBundler(2);

		if (size >= MBYTES) {
			sb.append(Math.round(size / (float)MBYTES));
			sb.append(" MB");
		}
		else if (size >= KBYTES) {
			sb.append(Math.round(size / (float)KBYTES));
			sb.append(" KB");
		}
		else {
			sb.append(1);
			sb.append(" KB");
		}

		return sb.toString();
	}

	private String _getTikaRawMetadataField(
			SearchRequestContext searchRequestContext, String key)
		throws Exception {

		StringBundler sb = new StringBundler(4);

		sb.append("ddm__text__");
		sb.append(String.valueOf(_getTikaRawStructureId(searchRequestContext)));
		sb.append("__TIFF_IMAGE_");
		sb.append(key);

		return sb.toString();
	}

	private long _getTikaRawStructureId(
			SearchRequestContext searchRequestContext)
		throws Exception {

		long companyId = searchRequestContext.getCompanyId();

		if ((TIKARAW_STRUCTURE_ID_MAP == null) ||
			(TIKARAW_STRUCTURE_ID_MAP.get(companyId) == null)) {

			DynamicQuery structureQuery =
				_ddmStructureLocalService.dynamicQuery();

			structureQuery.add(
				RestrictionsFactoryUtil.eq("structureKey", "TIKARAWMETADATA"));
			structureQuery.add(
				RestrictionsFactoryUtil.eq("companyId", companyId));

			List<DDMStructure> structures =
				DDMStructureLocalServiceUtil.dynamicQuery(structureQuery);

			DDMStructure structure = structures.get(0);

			TIKARAW_STRUCTURE_ID_MAP = new HashMap<>();

			TIKARAW_STRUCTURE_ID_MAP.put(companyId, structure.getStructureId());
		}

		return TIKARAW_STRUCTURE_ID_MAP.get(companyId);
	}

	private void _setImageMetadata(
			SearchRequestContext searchRequestContext, Document document,
			Map<String, String> metaData)
		throws Exception {

		// Dimensions

		StringBundler sb = new StringBundler(4);

		sb.append(
			document.getString(
				_getTikaRawMetadataField(searchRequestContext, "WIDTH")));
		sb.append(" x ");
		sb.append(
			document.getString(
				_getTikaRawMetadataField(searchRequestContext, "LENGTH")));
		sb.append(" px");

		metaData.put("dimensions", sb.toString());
	}

	private String _translateMimetype(String mimeType) {
		if (_mimeTypes.containsKey(mimeType)) {
			return _mimeTypes.get(mimeType);
		}
		else if (mimeType.startsWith("application_")) {
			return mimeType.split("application_")[1];
		}
		else if (mimeType.startsWith("image_")) {
			return mimeType.split("image_")[1];
		}
		else if (mimeType.startsWith("text_")) {
			return mimeType.split("text_")[1];
		}
		else if (mimeType.startsWith("video_")) {
			return mimeType.split("video_")[1];
		}

		return mimeType;
	}

	private static final String _NAME = DLFileEntry.class.getName();

	private static final Map<String, String> _mimeTypes =
		new HashMap<String, String>() {
			{
				put(
					"application_vnd.openxmlformats-officedocument.presentationml.presentation",
					"pptx");
				put(
					"application_vnd.openxmlformats-officedocument.spreadsheetml.sheet",
					"xlsx");
				put(
					"application_vnd.openxmlformats-officedocument.wordprocessingml.document",
					"docx");

				put("application_vnd.ms-excel", "xls");
				put("application_vnd.ms-powerpoint", "ppt");
				put("application_vnd.ms-word", "doc");

				put("application_vnd.oasis.opendocument.presentation", "odp");
				put("application_vnd.oasis.opendocument.spreadsheet", "ods");
				put("application_vnd.oasis.opendocument.text", "odt");
			}
		};

	private Map<Long, Long> TIKARAW_STRUCTURE_ID_MAP;

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DLAppService _dLAppService;

}