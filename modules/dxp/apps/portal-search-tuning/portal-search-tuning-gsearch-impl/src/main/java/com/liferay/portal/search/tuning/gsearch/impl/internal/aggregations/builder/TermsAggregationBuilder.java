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

package com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.builder;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.aggregation.bucket.CollectionMode;
import com.liferay.portal.search.aggregation.bucket.IncludeExcludeClause;
import com.liferay.portal.search.aggregation.bucket.Order;
import com.liferay.portal.search.aggregation.bucket.TermsAggregation;
import com.liferay.portal.search.script.ScriptBuilder;
import com.liferay.portal.search.script.ScriptType;
import com.liferay.portal.search.script.Scripts;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.keys.AggregationConfigurationKeys;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.impl.internal.aggregations.AggregationBuilderFactory;
import com.liferay.portal.search.tuning.gsearch.impl.util.GSearchJsonUtil;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.spi.aggregation.AggregationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "type=terms",
	service = AggregationBuilder.class
)
public class TermsAggregationBuilder implements AggregationBuilder {

	@Override
	public Optional<Aggregation> build(
		SearchRequestContext searchRequestContext,
		JSONObject aggregationConfigurationJsonObject, String name) {

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Building terms aggregation with configuration [ " +
					aggregationConfigurationJsonObject + " ]");
		}

		if (!_validate(
				searchRequestContext, aggregationConfigurationJsonObject,
				name)) {

			return Optional.empty();
		}

		String field = aggregationConfigurationJsonObject.getString(
			AggregationConfigurationKeys.FIELD.getJsonKey());

		TermsAggregation aggregation = _aggregations.terms(name, field);

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.AGGREGATIONS.getJsonKey()))) {

			_processChildAggregations(
				searchRequestContext, aggregation,
				aggregationConfigurationJsonObject.getJSONArray(
					AggregationConfigurationKeys.AGGREGATIONS.getJsonKey()));
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.COLLECT_MODE.getJsonKey()))) {

			_setCollectMode(
				searchRequestContext, aggregation,
				aggregationConfigurationJsonObject);
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.EXECUTION_HINT.
						getJsonKey()))) {

			aggregation.setExecutionHint(
				aggregationConfigurationJsonObject.getString(
					AggregationConfigurationKeys.EXECUTION_HINT.getJsonKey()));
		}

		_setIncludeExcludeClause(
			aggregation, aggregationConfigurationJsonObject);

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.MIN_DOC_COUNT.getJsonKey()))) {

			aggregation.setMinDocCount(
				aggregationConfigurationJsonObject.getInt(
					AggregationConfigurationKeys.MIN_DOC_COUNT.getJsonKey()));
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.MISSING.getJsonKey()))) {

			aggregation.setMissing(
				aggregationConfigurationJsonObject.getString(
					AggregationConfigurationKeys.MISSING.getJsonKey()));
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.ORDER.getJsonKey()))) {

			_setOrders(
				searchRequestContext, aggregation,
				aggregationConfigurationJsonObject);
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.SCRIPT.getJsonKey()))) {

			_setScript(
				searchRequestContext, aggregation,
				aggregationConfigurationJsonObject);
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.SHARD_MIN_DOC_COUNT.
						getJsonKey()))) {

			aggregation.setShardMinDocCount(
				aggregationConfigurationJsonObject.getInt(
					AggregationConfigurationKeys.SHARD_MIN_DOC_COUNT.
						getJsonKey()));
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.SHARD_SIZE.getJsonKey()))) {

			aggregation.setShardSize(
				aggregationConfigurationJsonObject.getInt(
					AggregationConfigurationKeys.SHARD_SIZE.getJsonKey()));
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.SHOW_TERM_DOC_COUNT_ERROR.
						getJsonKey()))) {

			aggregation.setShowTermDocCountError(
				aggregationConfigurationJsonObject.getBoolean(
					AggregationConfigurationKeys.SHOW_TERM_DOC_COUNT_ERROR.
						getJsonKey()));
		}

		if (Validator.isNotNull(
				aggregationConfigurationJsonObject.get(
					AggregationConfigurationKeys.SIZE.getJsonKey()))) {

			aggregation.setSize(
				aggregationConfigurationJsonObject.getInt(
					AggregationConfigurationKeys.SIZE.getJsonKey()));
		}

		return Optional.of(aggregation);
	}

	private Order _getOrder(
		SearchRequestContext searchRequestContext, JSONObject orderJsonObject) {

		String orderMetric;

		try {
			orderMetric = orderJsonObject.keys(
			).next();
		}
		catch (NoSuchElementException noSuchElementException) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.invalid-aggregation-order-syntax",
					noSuchElementException.getMessage(), noSuchElementException,
					orderJsonObject,
					AggregationConfigurationKeys.ORDER.getJsonKey(), null));

			if (_log.isWarnEnabled()) {
				_log.warn(
					noSuchElementException.getMessage(),
					noSuchElementException);
			}

			return null;
		}

		String orderDirection = orderJsonObject.getString(orderMetric);

		Order order;

		if (Order.COUNT_METRIC_NAME.equals(orderMetric) ||
			Order.KEY_METRIC_NAME.equals(orderMetric)) {

			order = new Order(null);
			order.setMetricName(orderMetric);
		}
		else {
			String[] arr = orderMetric.split(".");

			if (arr.length != 2) {
				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.invalid-aggregation-order-syntax", null,
						null, orderJsonObject,
						AggregationConfigurationKeys.ORDER.getJsonKey(),
						orderMetric));

				return null;
			}

			String path = arr[0];
			String metric = arr[1];

			order = new Order(path);
			order.setMetricName(metric);
		}

		order.setAscending(orderDirection.equalsIgnoreCase("asc"));

		return order;
	}

	private void _processChildAggregations(
		SearchRequestContext searchRequestContext, TermsAggregation aggregation,
		JSONArray childAggregationJsonArray) {

		for (int i = 0; i < childAggregationJsonArray.length(); i++) {
			JSONObject childAggregationJsonObject =
				childAggregationJsonArray.getJSONObject(i);

			String type = childAggregationJsonObject.getString(
				AggregationConfigurationKeys.TYPE.getJsonKey());
			String name = childAggregationJsonObject.getString(
				AggregationConfigurationKeys.NAME.getJsonKey());

			if (!_validate(
					searchRequestContext, childAggregationJsonObject, name)) {

				continue;
			}

			try {
				AggregationBuilder aggregationBuilder =
					_aggregationBuilderFactory.getBuilder(type);

				Optional<Aggregation> aggregationOptional =
					aggregationBuilder.build(
						searchRequestContext, childAggregationJsonObject, name);

				if (aggregationOptional.isPresent()) {
					aggregation.addChildAggregation(aggregationOptional.get());
				}
			}
			catch (IllegalArgumentException illegalArgumentException) {
				searchRequestContext.addMessage(
					new Message(
						Severity.ERROR, "core",
						"core.error.invalid-aggregation-builder-type",
						illegalArgumentException.getMessage(),
						illegalArgumentException, childAggregationJsonObject,
						AggregationConfigurationKeys.AGGREGATIONS.getJsonKey(),
						type));

				if (_log.isWarnEnabled()) {
					_log.warn(
						illegalArgumentException.getMessage(),
						illegalArgumentException);
				}
			}
		}
	}

	private void _setCollectMode(
		SearchRequestContext searchRequestContext, TermsAggregation aggregation,
		JSONObject aggregationConfigurationJsonObject) {

		String collectModeString = aggregationConfigurationJsonObject.getString(
			AggregationConfigurationKeys.COLLECT_MODE.getJsonKey());

		try {
			CollectionMode collectMode = CollectionMode.valueOf(
				collectModeString.toUpperCase());

			aggregation.setCollectionMode(collectMode);
		}
		catch (IllegalArgumentException illegalArgumentException) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.invalid-aggregation-collect-mode",
					illegalArgumentException.getMessage(),
					illegalArgumentException,
					aggregationConfigurationJsonObject,
					AggregationConfigurationKeys.COLLECT_MODE.getJsonKey(),
					collectModeString));

			if (_log.isWarnEnabled()) {
				_log.warn(
					illegalArgumentException.getMessage(),
					illegalArgumentException);
			}
		}
	}

	private void _setIncludeExcludeClause(
		TermsAggregation aggregation,
		JSONObject aggregationConfigurationJsonObject) {

		Object excludeObject = aggregationConfigurationJsonObject.get(
			AggregationConfigurationKeys.EXCLUDE.getJsonKey());
		Object includeObject = aggregationConfigurationJsonObject.get(
			AggregationConfigurationKeys.INCLUDE.getJsonKey());

		if (Validator.isNotNull(excludeObject) ||
			Validator.isNotNull(includeObject)) {

			IncludeExcludeClause includeExcludeClause = null;

			if ((excludeObject instanceof JSONArray) ||
				(includeObject instanceof JSONArray)) {

				String[] excludeArray = GSearchJsonUtil.jsonArrayToStringArray(
					(JSONArray)excludeObject);
				String[] includeArray = GSearchJsonUtil.jsonArrayToStringArray(
					(JSONArray)includeObject);

				includeExcludeClause = new IncludeExcludeClause() {

					@Override
					public String[] getExcludedValues() {
						return excludeArray;
					}

					@Override
					public String getExcludeRegex() {
						return null;
					}

					@Override
					public String[] getIncludedValues() {
						return includeArray;
					}

					@Override
					public String getIncludeRegex() {
						return null;
					}

				};
			}
			else if ((excludeObject instanceof String) ||
					 (includeObject instanceof String)) {

				includeExcludeClause = new IncludeExcludeClause() {

					@Override
					public String[] getExcludedValues() {
						return null;
					}

					@Override
					public String getExcludeRegex() {
						return (String)excludeObject;
					}

					@Override
					public String[] getIncludedValues() {
						return null;
					}

					@Override
					public String getIncludeRegex() {
						return (String)includeObject;
					}

				};
			}

			aggregation.setIncludeExcludeClause(includeExcludeClause);
		}
	}

	// Todo: https://issues.liferay.com/browse/LPS-88706

	private void _setOrders(
		SearchRequestContext searchRequestContext, TermsAggregation aggregation,
		JSONObject aggregationConfigurationJsonObject) {

		Object orderObject = aggregationConfigurationJsonObject.get(
			AggregationConfigurationKeys.ORDER.getJsonKey());

		List<Order> orders = new ArrayList<>();

		if (orderObject instanceof JSONArray) {
			((JSONArray)orderObject).forEach(
				object -> {
					JSONObject orderJsonObject = (JSONObject)object;

					Order order = _getOrder(
						searchRequestContext, orderJsonObject);

					orders.add(order);
				});
		}
		else if (orderObject instanceof JSONObject) {
			JSONObject orderJsonObject = (JSONObject)orderObject;

			Order order = _getOrder(searchRequestContext, orderJsonObject);

			orders.add(order);
		}

		aggregation.addOrders(
			orders.stream(
			).toArray(
				Order[]::new
			));
	}

	private void _setScript(
		SearchRequestContext searchRequestContext, TermsAggregation aggregation,
		JSONObject aggregationConfigurationJsonObject) {

		JSONObject scriptJsonObject =
			aggregationConfigurationJsonObject.getJSONObject(
				AggregationConfigurationKeys.SCRIPT.getJsonKey());

		ScriptBuilder scriptBuilder = _scripts.builder();

		if (Validator.isNotNull(
				scriptJsonObject.get(
					AggregationConfigurationKeys.ID.getJsonKey()))) {

			String id = scriptJsonObject.getString(
				AggregationConfigurationKeys.ID.getJsonKey());

			scriptBuilder.idOrCode(
				id
			).scriptType(
				ScriptType.STORED
			);
		}
		else if (Validator.isNotNull(
					scriptJsonObject.get(
						AggregationConfigurationKeys.SOURCE.getJsonKey()))) {

			String source = scriptJsonObject.getString(
				AggregationConfigurationKeys.SOURCE.getJsonKey());

			scriptBuilder.idOrCode(
				source
			).scriptType(
				ScriptType.INLINE
			);
		}
		else {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.aggregation-script-id-or-source-missing", null,
					null, scriptJsonObject,
					AggregationConfigurationKeys.SCRIPT.getJsonKey(), null));

			return;
		}

		if (Validator.isNotNull(
				scriptJsonObject.get(
					AggregationConfigurationKeys.LANG.getJsonKey()))) {

			scriptBuilder.language(
				scriptJsonObject.getString(
					AggregationConfigurationKeys.LANG.getJsonKey()));
		}

		if (Validator.isNotNull(
				scriptJsonObject.get(
					AggregationConfigurationKeys.PARAMS.getJsonKey()))) {

			JSONObject paramsJsonObject = scriptJsonObject.getJSONObject(
				AggregationConfigurationKeys.PARAMS.getJsonKey());

			paramsJsonObject.keySet(
			).stream(
			).forEach(
				key -> scriptBuilder.putParameter(
					key, paramsJsonObject.get(key))
			);
		}

		if (Validator.isNotNull(
				scriptJsonObject.get(
					AggregationConfigurationKeys.OPTIONS.getJsonKey()))) {

			JSONObject optionsJsonObject = scriptJsonObject.getJSONObject(
				AggregationConfigurationKeys.OPTIONS.getJsonKey());

			optionsJsonObject.keySet(
			).stream(
			).forEach(
				key -> scriptBuilder.putParameter(
					key, optionsJsonObject.get(key))
			);
		}

		aggregation.setScript(scriptBuilder.build());
	}

	private boolean _validate(
		SearchRequestContext searchRequestContext,
		JSONObject aggregationConfigurationJsonObject, String name) {

		boolean valid = true;

		if (aggregationConfigurationJsonObject == null) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.aggregation-configuration-not-found", null,
					null, aggregationConfigurationJsonObject,
					AggregationConfigurationKeys.BODY.getJsonKey(), null));
			valid = false;
		}

		if (Validator.isBlank(name)) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.aggregation-name-empty",
					null, null, null,
					AggregationConfigurationKeys.NAME.getJsonKey(), null));
			valid = false;
		}

		if (Validator.isNull(
				aggregationConfigurationJsonObject.getString(
					AggregationConfigurationKeys.FIELD.getJsonKey()))) {

			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.aggregation-field-not-defined", null, null,
					aggregationConfigurationJsonObject,
					AggregationConfigurationKeys.FIELD.getJsonKey(), null));
			valid = false;
		}

		return valid;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TermsAggregationBuilder.class);

	@Reference
	private AggregationBuilderFactory _aggregationBuilderFactory;

	@Reference
	private Aggregations _aggregations;

	@Reference
	private Scripts _scripts;

}