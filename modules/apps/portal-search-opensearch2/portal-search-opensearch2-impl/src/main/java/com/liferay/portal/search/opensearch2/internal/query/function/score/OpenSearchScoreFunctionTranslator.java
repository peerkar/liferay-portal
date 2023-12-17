/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.opensearch2.internal.query.function.score;

import com.liferay.portal.search.opensearch2.internal.query.OpenSearchQueryTranslator;
import com.liferay.portal.search.opensearch2.internal.script.ScriptTranslator;
import com.liferay.portal.search.opensearch2.internal.util.SetterUtil;
import com.liferay.portal.search.query.FunctionScoreQuery.FilterQueryScoreFunctionHolder;
import com.liferay.portal.search.query.MultiValueMode;
import com.liferay.portal.search.query.function.score.ExponentialDecayScoreFunction;
import com.liferay.portal.search.query.function.score.FieldValueFactorScoreFunction;
import com.liferay.portal.search.query.function.score.GaussianDecayScoreFunction;
import com.liferay.portal.search.query.function.score.LinearDecayScoreFunction;
import com.liferay.portal.search.query.function.score.RandomScoreFunction;
import com.liferay.portal.search.query.function.score.ScoreFunction;
import com.liferay.portal.search.query.function.score.ScoreFunctionTranslator;
import com.liferay.portal.search.query.function.score.ScriptScoreFunction;
import com.liferay.portal.search.query.function.score.WeightScoreFunction;

import java.util.function.Consumer;

import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.query_dsl.DecayFunction;
import org.opensearch.client.opensearch._types.query_dsl.DecayPlacement;
import org.opensearch.client.opensearch._types.query_dsl.FieldValueFactorModifier;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScore;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScoreBuilders;
import org.opensearch.client.opensearch._types.query_dsl.FunctionScoreVariant;
import org.opensearch.client.opensearch._types.query_dsl.Query;

/**
 * @author Michael C. Han
 * @author Petteri Karttunen
 */
public class OpenSearchScoreFunctionTranslator
	implements ScoreFunctionTranslator<FunctionScoreVariant> {

	public OpenSearchScoreFunctionTranslator(
		FilterQueryScoreFunctionHolder filterQueryScoreFunctionHolder,
		OpenSearchQueryTranslator openSearchQueryTranslator) {

		_filterQueryScoreFunctionHolder = filterQueryScoreFunctionHolder;
		_openSearchQueryTranslator = openSearchQueryTranslator;
	}

	public FunctionScore translate() {
		ScoreFunction scoreFunction =
			_filterQueryScoreFunctionHolder.getScoreFunction();

		if (scoreFunction == null) {
			return null;
		}

		return new FunctionScore(scoreFunction.accept(this));
	}

	@Override
	public FunctionScoreVariant translate(
		ExponentialDecayScoreFunction exponentialDecayScoreFunction) {

		DecayFunction.Builder decayFunctionBuilder =
			FunctionScoreBuilders.exp();

		decayFunctionBuilder.field(exponentialDecayScoreFunction.getField());
		decayFunctionBuilder.multiValueMode(
			_translateMultiValueMode(
				exponentialDecayScoreFunction.getMultiValueMode()));

		DecayPlacement.Builder decayPlacementBuilder =
			new DecayPlacement.Builder();

		decayPlacementBuilder.offset(
			JsonData.of(exponentialDecayScoreFunction.getOffset()));
		decayPlacementBuilder.origin(
			JsonData.of(exponentialDecayScoreFunction.getOrigin()));
		decayPlacementBuilder.scale(
			JsonData.of(exponentialDecayScoreFunction.getScale()));

		SetterUtil.setNotNullDouble(
			decayPlacementBuilder::decay,
			exponentialDecayScoreFunction.getDecay());

		decayFunctionBuilder.placement(decayPlacementBuilder.build());

		SetterUtil.setNotNullFloatAsDouble(
			decayFunctionBuilder::weight,
			exponentialDecayScoreFunction.getWeight());

		_addFilterQuery(decayFunctionBuilder::filter);

		return decayFunctionBuilder.build();
	}

	@Override
	public FunctionScoreVariant translate(
		FieldValueFactorScoreFunction fieldValueFactorScoreFunction) {

		org.opensearch.client.opensearch._types.query_dsl.
			FieldValueFactorScoreFunction.Builder
				fieldValueFactorScoreFunctionBuilder =
					FunctionScoreBuilders.fieldValueFactor();

		fieldValueFactorScoreFunctionBuilder.field(
			fieldValueFactorScoreFunction.getField());

		SetterUtil.setNotNullFloatAsDouble(
			fieldValueFactorScoreFunctionBuilder::factor,
			fieldValueFactorScoreFunction.getFactor());
		SetterUtil.setNotNullDouble(
			fieldValueFactorScoreFunctionBuilder::missing,
			fieldValueFactorScoreFunction.getMissing());

		if (fieldValueFactorScoreFunction.getModifier() != null) {
			fieldValueFactorScoreFunctionBuilder.modifier(
				_translateModifier(
					fieldValueFactorScoreFunction.getModifier()));
		}

		SetterUtil.setNotNullFloatAsDouble(
			fieldValueFactorScoreFunctionBuilder::weight,
			fieldValueFactorScoreFunction.getWeight());

		_addFilterQuery(fieldValueFactorScoreFunctionBuilder::filter);

		return fieldValueFactorScoreFunctionBuilder.build();
	}

	@Override
	public FunctionScoreVariant translate(
		GaussianDecayScoreFunction gaussianDecayScoreFunction) {

		DecayFunction.Builder decayFunctionBuilder =
			FunctionScoreBuilders.gauss();

		decayFunctionBuilder.field(gaussianDecayScoreFunction.getField());
		decayFunctionBuilder.multiValueMode(
			_translateMultiValueMode(
				gaussianDecayScoreFunction.getMultiValueMode()));

		DecayPlacement.Builder decayPlacementBuilder =
			new DecayPlacement.Builder();

		decayPlacementBuilder.offset(
			JsonData.of(gaussianDecayScoreFunction.getOffset()));
		decayPlacementBuilder.origin(
			JsonData.of(gaussianDecayScoreFunction.getOrigin()));
		decayPlacementBuilder.scale(
			JsonData.of(gaussianDecayScoreFunction.getScale()));

		SetterUtil.setNotNullDouble(
			decayPlacementBuilder::decay,
			gaussianDecayScoreFunction.getDecay());

		decayFunctionBuilder.placement(decayPlacementBuilder.build());

		SetterUtil.setNotNullFloatAsDouble(
			decayFunctionBuilder::weight,
			gaussianDecayScoreFunction.getWeight());

		_addFilterQuery(decayFunctionBuilder::filter);

		return decayFunctionBuilder.build();
	}

	@Override
	public FunctionScoreVariant translate(
		LinearDecayScoreFunction linearDecayScoreFunction) {

		DecayFunction.Builder decayFunctionBuilder =
			FunctionScoreBuilders.linear();

		decayFunctionBuilder.field(linearDecayScoreFunction.getField());
		decayFunctionBuilder.multiValueMode(
			_translateMultiValueMode(
				linearDecayScoreFunction.getMultiValueMode()));

		DecayPlacement.Builder decayPlacementBuilder =
			new DecayPlacement.Builder();

		decayPlacementBuilder.offset(
			JsonData.of(linearDecayScoreFunction.getOffset()));
		decayPlacementBuilder.origin(
			JsonData.of(linearDecayScoreFunction.getOrigin()));
		decayPlacementBuilder.scale(
			JsonData.of(linearDecayScoreFunction.getScale()));

		SetterUtil.setNotNullDouble(
			decayPlacementBuilder::decay, linearDecayScoreFunction.getDecay());

		decayFunctionBuilder.placement(decayPlacementBuilder.build());

		SetterUtil.setNotNullFloatAsDouble(
			decayFunctionBuilder::weight, linearDecayScoreFunction.getWeight());

		_addFilterQuery(decayFunctionBuilder::filter);

		return decayFunctionBuilder.build();
	}

	@Override
	public FunctionScoreVariant translate(
		RandomScoreFunction randomScoreFunction) {

		org.opensearch.client.opensearch._types.query_dsl.RandomScoreFunction.
			Builder randomScoreFunctionBuilder =
				FunctionScoreBuilders.randomScore();

		SetterUtil.setNotBlankString(
			randomScoreFunctionBuilder::field, randomScoreFunction.getField());
		SetterUtil.setNotBlankString(
			randomScoreFunctionBuilder::seed,
			String.valueOf(randomScoreFunction.getSeed()));

		SetterUtil.setNotNullFloatAsDouble(
			randomScoreFunctionBuilder::weight,
			randomScoreFunction.getWeight());

		_addFilterQuery(randomScoreFunctionBuilder::filter);

		return randomScoreFunctionBuilder.build();
	}

	@Override
	public FunctionScoreVariant translate(
		ScriptScoreFunction scriptScoreFunction) {

		org.opensearch.client.opensearch._types.query_dsl.ScriptScoreFunction.
			Builder scriptScoreFunctionBuilder =
				FunctionScoreBuilders.scriptScore();

		scriptScoreFunctionBuilder.script(
			_scriptTranslator.translate(scriptScoreFunction.getScript()));

		SetterUtil.setNotNullFloatAsDouble(
			scriptScoreFunctionBuilder::weight,
			scriptScoreFunction.getWeight());

		_addFilterQuery(scriptScoreFunctionBuilder::filter);

		return scriptScoreFunctionBuilder.build();
	}

	@Override
	public FunctionScoreVariant translate(
		WeightScoreFunction weightScoreFunction) {

		throw new UnsupportedOperationException();
	}

	private void _addFilterQuery(Consumer<Query> consumer) {
		if (_filterQueryScoreFunctionHolder.getFilterQuery() != null) {
			consumer.accept(
				new Query(
					_openSearchQueryTranslator.translate(
						_filterQueryScoreFunctionHolder.getFilterQuery())));
		}
	}

	private FieldValueFactorModifier _translateModifier(
		FieldValueFactorScoreFunction.Modifier modifier) {

		if (modifier == FieldValueFactorScoreFunction.Modifier.LN) {
			return FieldValueFactorModifier.Ln;
		}
		else if (modifier == FieldValueFactorScoreFunction.Modifier.LN1P) {
			return FieldValueFactorModifier.Ln1p;
		}
		else if (modifier == FieldValueFactorScoreFunction.Modifier.LN2P) {
			return FieldValueFactorModifier.Ln2p;
		}
		else if (modifier == FieldValueFactorScoreFunction.Modifier.LOG) {
			return FieldValueFactorModifier.Log;
		}
		else if (modifier == FieldValueFactorScoreFunction.Modifier.LOG1P) {
			return FieldValueFactorModifier.Log1p;
		}
		else if (modifier == FieldValueFactorScoreFunction.Modifier.LOG2P) {
			return FieldValueFactorModifier.Log2p;
		}
		else if (modifier == FieldValueFactorScoreFunction.Modifier.NONE) {
			return FieldValueFactorModifier.None;
		}
		else if (modifier ==
					FieldValueFactorScoreFunction.Modifier.RECIPROCAL) {

			return FieldValueFactorModifier.Reciprocal;
		}
		else if (modifier == FieldValueFactorScoreFunction.Modifier.SQRT) {
			return FieldValueFactorModifier.Sqrt;
		}
		else if (modifier == FieldValueFactorScoreFunction.Modifier.SQUARE) {
			return FieldValueFactorModifier.Square;
		}

		throw new IllegalArgumentException("Invalid modifier " + modifier);
	}

	private org.opensearch.client.opensearch._types.query_dsl.MultiValueMode
		_translateMultiValueMode(MultiValueMode multiValueMode) {

		if (multiValueMode == MultiValueMode.AVG) {
			return org.opensearch.client.opensearch._types.query_dsl.
				MultiValueMode.Avg;
		}
		else if (multiValueMode == MultiValueMode.MAX) {
			return org.opensearch.client.opensearch._types.query_dsl.
				MultiValueMode.Max;
		}
		else if (multiValueMode == MultiValueMode.MIN) {
			return org.opensearch.client.opensearch._types.query_dsl.
				MultiValueMode.Min;
		}
		else if (multiValueMode == MultiValueMode.SUM) {
			return org.opensearch.client.opensearch._types.query_dsl.
				MultiValueMode.Sum;
		}

		throw new IllegalArgumentException(
			"Invalid multi value mode " + multiValueMode);
	}

	private final FilterQueryScoreFunctionHolder
		_filterQueryScoreFunctionHolder;
	private final OpenSearchQueryTranslator _openSearchQueryTranslator;
	private final ScriptTranslator _scriptTranslator = new ScriptTranslator();

}