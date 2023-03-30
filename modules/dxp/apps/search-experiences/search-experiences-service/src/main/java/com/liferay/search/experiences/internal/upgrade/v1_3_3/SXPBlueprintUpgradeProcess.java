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

package com.liferay.search.experiences.internal.upgrade.v1_3_3;

import com.liferay.petra.io.StreamUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.search.experiences.internal.model.listener.CompanyModelListener;
import com.liferay.search.experiences.rest.dto.v1_0.ElementDefinition;
import com.liferay.search.experiences.rest.dto.v1_0.Field;
import com.liferay.search.experiences.rest.dto.v1_0.FieldSet;
import com.liferay.search.experiences.rest.dto.v1_0.SXPElement;
import com.liferay.search.experiences.rest.dto.v1_0.UiConfiguration;
import com.liferay.search.experiences.rest.dto.v1_0.util.SXPElementUtil;

import java.io.IOException;

import java.net.URL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Felipe Lorenz
 */
public class SXPBlueprintUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		_upgradeSXPBlueprints();

		_upgradeSXPElements();
	}

	private Map<String, SXPElement> _createSXPElements() {
		if (_sxpElements != null) {
			return _sxpElements;
		}

		Bundle bundle = FrameworkUtil.getBundle(CompanyModelListener.class);

		Package pkg = CompanyModelListener.class.getPackage();

		String path = StringUtil.replace(
			pkg.getName(), CharPool.PERIOD, CharPool.SLASH);

		_sxpElements = new HashMap<>();

		Enumeration<URL> enumeration = bundle.findEntries(
			path.concat("/dependencies"), "*.json", false);

		try {
			while (enumeration.hasMoreElements()) {
				URL url = enumeration.nextElement();

				SXPElement sxpElement = SXPElementUtil.toSXPElement(
					StreamUtil.toString(url.openStream()));

				_sxpElements.put(
					sxpElement.getExternalReferenceCode(), sxpElement);
			}
		}
		catch (IOException ioException) {
			_log.error(ioException);
		}

		return _sxpElements;
	}

	private String _upgradeElementInstancesJSON(String elementInstancesJSON)
		throws Exception {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
			elementInstancesJSON);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject1 = jsonArray.getJSONObject(i);

			JSONObject sxpElementJSONObject = jsonObject1.getJSONObject(
				"sxpElement");

			Map<String, SXPElement> sxpElements = _createSXPElements();

			SXPElement sxpElement = sxpElements.get(
				sxpElementJSONObject.getString("externalReferenceCode"));

			if (sxpElement == null) {
				continue;
			}

			JSONObject elementDefinitionJSONObject =
				sxpElementJSONObject.getJSONObject(
					"elementDefinition");

			JSONObject uiConfigurationJSONObject =
				elementDefinitionJSONObject.getJSONObject(
					"uiConfiguration");

			JSONArray fieldSetsJSONObject =
				uiConfigurationJSONObject.getJSONArray("fieldSets");

			ElementDefinition elementDefinition =
				sxpElement.getElementDefinition();

			UiConfiguration uiConfiguration =
				elementDefinition.getUiConfiguration();

			FieldSet[] fieldSets = uiConfiguration.getFieldSets();

			for (FieldSet fieldSet : fieldSets) {
				Field[] fields = fieldSet.getFields();

				//the function getCorrectHelpText will come here like -> String correctHelpText = getCorrectHelpText...
				//the function getIncorrectHelpText will come here -> JSONObject wrongJSONObject = getIncorrectHelpText..

				wrongJSONObject.put( //we will have the JSON object who contains the helpText that we want to change
					"helpText", correctHelpText); //we will have the correct helpText value

				wrongJSONObject.put(
					"label", correctLabel);// we will have the correct label value (like the help text, create a function to get the correct labelValue)
			}
		}
		return jsonArray.toString();
	}

	private String getCorrectHelpText (int indexFieldSet, int index, UiConfiguration uiConfiguration){

		FieldSet[] fieldSets = uiConfiguration.getFieldSets();

		FieldSet fieldSet = fieldSets[indexFieldSet];

			for (Field field : fields) {

				if (!Validator.isBlank(field.getHelpText())){

					return field.getHelpText();
				}
			}
		return "";
	}

	private JSONObject getIncorrectHelpText(int indexFieldSet, int indexField, JSONArray fieldSetsJSONObject){

		JSONObject jsonObject3 = null;

		for (int y = 0; y < fieldSetsJSONObject.length(); y++) {
			JSONObject jsonObject2 =
				fieldSetsJSONObject.getJSONObject(y);

			JSONArray fieldsJSONObject =
				jsonObject2.getJSONArray("fields");

			for (int f = 0; f < fieldsJSONObject.length(); f++) {

				jsonObject3 =
					fieldsJSONObject.getJSONObject(f);

			}
		}

		return jsonObject3;
	}


	private void _upgradeSXPBlueprints() throws Exception {
		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
				"select sxpBlueprintId, elementInstancesJSON from " +
					"SXPBlueprint");
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update SXPBlueprint set elementInstancesJSON = ? where " +
						"sxpBlueprintId = ?")) {

			try (ResultSet resultSet = preparedStatement1.executeQuery()) {
				while (resultSet.next()) {
					try {
						preparedStatement2.setString(
							1,
							_upgradeElementInstancesJSON(
								resultSet.getString("elementInstancesJSON")));
						preparedStatement2.setLong(
							2, resultSet.getLong("sxpBlueprintId"));

						preparedStatement2.addBatch();
					}
					catch (RuntimeException runtimeException) {
						_log.error(
							StringBundler.concat(
								"Search experiences blueprint ",
								resultSet.getLong("sxpBlueprintId"),
								" contains corrupted element instances JSON"),
							runtimeException);
					}
				}

				preparedStatement2.executeBatch();
			}
		}
	}

	private void _upgradeSXPElements() throws Exception {
		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
				"select externalReferenceCode, readOnly from SXPElement");
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update SXPElement set elementDefinitionJSON = ? where " +
					"externalReferenceCode = ?")) {

			try (ResultSet resultSet = preparedStatement1.executeQuery()) {
				while (resultSet.next()) {
					if (!resultSet.getBoolean("readOnly")) {
						continue;
					}

					Map<String, SXPElement> sxpElements = _createSXPElements();

					SXPElement sxpElement = sxpElements.get(
						resultSet.getString("externalReferenceCode"));

					if (sxpElement == null) {
						continue;
					}

					preparedStatement2.setString(
						1, String.valueOf(sxpElement.getElementDefinition()));

					preparedStatement2.setString(
						2, resultSet.getString("externalReferenceCode"));

					preparedStatement2.addBatch();
				}

				preparedStatement2.executeBatch();
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SXPBlueprintUpgradeProcess.class);

	private Map<String, SXPElement> _sxpElements;

}