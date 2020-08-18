/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.source.formatter.checkstyle.checks;

import com.liferay.portal.kernel.util.ArrayUtil;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hugo Huijser
 */
public abstract class BaseBuilderCheck extends BaseChainedMethodCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[] {
			TokenTypes.ASSIGN, TokenTypes.INSTANCE_INIT, TokenTypes.METHOD_CALL
		};
	}

	protected abstract boolean allowNullValues();

	@Override
	protected void doVisitToken(DetailAST detailAST) {
		if (isExcludedPath(RUN_OUTSIDE_PORTAL_EXCLUDES)) {
			return;
		}

		if (detailAST.getType() == TokenTypes.INSTANCE_INIT) {
			_checkAnonymousClass(detailAST);

			return;
		}

		if (detailAST.getType() == TokenTypes.METHOD_CALL) {
			_checkBuilder(detailAST);

			return;
		}

		DetailAST parentDetailAST = detailAST.getParent();

		if ((parentDetailAST.getType() != TokenTypes.EXPR) &&
			(parentDetailAST.getType() != TokenTypes.VARIABLE_DEF)) {

			return;
		}

		DetailAST nextSiblingDetailAST = parentDetailAST.getNextSibling();

		if ((nextSiblingDetailAST == null) ||
			(nextSiblingDetailAST.getType() != TokenTypes.SEMI)) {

			return;
		}

		String variableName = getVariableName(detailAST, parentDetailAST);

		if (variableName != null) {
			_checkAssignVariableStatement(
				detailAST, variableName, nextSiblingDetailAST);
		}
	}

	protected abstract String getAssignClassName(DetailAST assignDetailAST);

	protected abstract List<BuilderInformation> getBuilderInformationList();

	protected String getNewInstanceTypeName(DetailAST assignDetailAST) {
		DetailAST firstChildDetailAST = assignDetailAST.getFirstChild();

		DetailAST assignValueDetailAST = null;

		DetailAST parentDetailAST = assignDetailAST.getParent();

		if (parentDetailAST.getType() == TokenTypes.EXPR) {
			assignValueDetailAST = firstChildDetailAST.getNextSibling();
		}
		else {
			assignValueDetailAST = firstChildDetailAST.getFirstChild();
		}

		if ((assignValueDetailAST == null) ||
			(assignValueDetailAST.getType() != TokenTypes.LITERAL_NEW)) {

			return null;
		}

		DetailAST identDetailAST = assignValueDetailAST.findFirstToken(
			TokenTypes.IDENT);

		if (identDetailAST == null) {
			return null;
		}

		DetailAST elistDetailAST = assignValueDetailAST.findFirstToken(
			TokenTypes.ELIST);

		if ((elistDetailAST == null) ||
			(elistDetailAST.getFirstChild() != null)) {

			return null;
		}

		return identDetailAST.getText();
	}

	protected abstract List<String> getSupportsFunctionMethodNames();

	protected abstract boolean isSupportsNestedMethodCalls();

	protected static class BuilderInformation {

		public BuilderInformation(
			String className, String builderClassName, String... methodNames) {

			_className = className;
			_builderClassName = builderClassName;
			_methodNames = methodNames;
		}

		public String getBuilderClassName() {
			return _builderClassName;
		}

		public String getClassName() {
			return _className;
		}

		public String[] getMethodNames() {
			return _methodNames;
		}

		private final String _builderClassName;
		private final String _className;
		private final String[] _methodNames;

	}

	private void _checkAnonymousClass(DetailAST detailAST) {
		DetailAST parentDetailAST = detailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.OBJBLOCK) {
			return;
		}

		parentDetailAST = parentDetailAST.getParent();

		if (parentDetailAST.getType() != TokenTypes.LITERAL_NEW) {
			return;
		}

		DetailAST identDetailAST = parentDetailAST.findFirstToken(
			TokenTypes.IDENT);

		if (identDetailAST == null) {
			return;
		}

		String className = identDetailAST.getText();

		BuilderInformation builderInformation =
			_findBuilderInformationByClassName(className);

		if (builderInformation == null) {
			return;
		}

		List<DetailAST> childDetailASTList = getAllChildTokens(
			detailAST, true, ALL_TYPES);

		for (DetailAST childDetailAST : childDetailASTList) {
			if (getHiddenBefore(childDetailAST) != null) {
				return;
			}
		}

		List<DetailAST> methodCallDetailASTList = getAllChildTokens(
			detailAST, true, TokenTypes.METHOD_CALL);

		for (DetailAST methodCallDetailAST : methodCallDetailASTList) {
			parentDetailAST = methodCallDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.EXPR) {
				continue;
			}

			parentDetailAST = parentDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.SLIST) {
				continue;
			}

			DetailAST firstChildDetailAST = methodCallDetailAST.getFirstChild();

			if (firstChildDetailAST.getType() != TokenTypes.IDENT) {
				continue;
			}

			if (!ArrayUtil.contains(
					builderInformation.getMethodNames(),
					firstChildDetailAST.getText())) {

				return;
			}

			if (isSupportsNestedMethodCalls()) {
				parentDetailAST = getParentWithTokenType(
					methodCallDetailAST, TokenTypes.DO_WHILE, TokenTypes.LAMBDA,
					TokenTypes.LITERAL_FOR, TokenTypes.LITERAL_TRY,
					TokenTypes.LITERAL_WHILE);

				if ((parentDetailAST != null) &&
					(detailAST.getLineNo() <= parentDetailAST.getLineNo())) {

					return;
				}

				parentDetailAST = getParentWithTokenType(
					methodCallDetailAST, TokenTypes.LITERAL_ELSE);

				if ((parentDetailAST != null) &&
					(detailAST.getLineNo() <= parentDetailAST.getLineNo())) {

					firstChildDetailAST = parentDetailAST.getFirstChild();

					if (firstChildDetailAST.getType() ==
							TokenTypes.LITERAL_IF) {

						return;
					}
				}
			}
			else if (!equals(parentDetailAST.getParent(), detailAST)) {
				return;
			}

			if (!allowNullValues()) {
				DetailAST elistDetailAST = methodCallDetailAST.findFirstToken(
					TokenTypes.ELIST);

				DetailAST childDetailAST = elistDetailAST.getFirstChild();

				while (true) {
					if (childDetailAST == null) {
						break;
					}

					if (_isNullValueExpression(childDetailAST)) {
						return;
					}

					childDetailAST = childDetailAST.getNextSibling();
				}
			}
		}

		log(
			detailAST, _MSG_USE_BUILDER_INSTEAD,
			builderInformation.getBuilderClassName(), className);
	}

	private void _checkAssignVariableStatement(
		DetailAST assignDetailAST, String variableName,
		DetailAST nextSiblingDetailAST) {

		BuilderInformation builderInformation =
			_findBuilderInformationByClassName(
				getAssignClassName(assignDetailAST));

		if (builderInformation == null) {
			return;
		}

		while (true) {
			nextSiblingDetailAST = nextSiblingDetailAST.getNextSibling();

			if (nextSiblingDetailAST == null) {
				return;
			}

			FullIdent fullIdent = getMethodCallFullIdent(
				nextSiblingDetailAST, variableName,
				builderInformation.getMethodNames());

			if (fullIdent != null) {
				DetailAST methodCallDetailAST =
					nextSiblingDetailAST.findFirstToken(TokenTypes.METHOD_CALL);

				DetailAST elistDetailAST = methodCallDetailAST.findFirstToken(
					TokenTypes.ELIST);

				DetailAST childDetailAST = elistDetailAST.getFirstChild();

				while (true) {
					if (childDetailAST == null) {
						log(
							assignDetailAST, _MSG_USE_BUILDER,
							builderInformation.getBuilderClassName(),
							assignDetailAST.getLineNo(), fullIdent.getLineNo());

						return;
					}

					if (!allowNullValues() &&
						_isNullValueExpression(childDetailAST)) {

						return;
					}

					childDetailAST = childDetailAST.getNextSibling();
				}
			}

			if (containsVariableName(nextSiblingDetailAST, variableName)) {
				return;
			}
		}
	}

	private void _checkBuilder(DetailAST methodCallDetailAST) {
		DetailAST firstChildDetailAST = methodCallDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() != TokenTypes.DOT) {
			return;
		}

		firstChildDetailAST = firstChildDetailAST.getFirstChild();

		if (firstChildDetailAST.getType() != TokenTypes.IDENT) {
			return;
		}

		String builderClassName = firstChildDetailAST.getText();

		BuilderInformation builderInformation =
			_findBuilderInformationByBuilderClassName(builderClassName);

		if (builderInformation == null) {
			return;
		}

		Map<String, List<DetailAST>> expressionDetailASTMap =
			_getExpressionDetailASTMap(methodCallDetailAST);

		if (!allowNullValues()) {
			_checkNullValues(expressionDetailASTMap, builderClassName);
		}

		DetailAST parentDetailAST = methodCallDetailAST.getParent();

		while ((parentDetailAST.getType() == TokenTypes.DOT) ||
			   (parentDetailAST.getType() == TokenTypes.EXPR) ||
			   (parentDetailAST.getType() == TokenTypes.METHOD_CALL)) {

			parentDetailAST = parentDetailAST.getParent();
		}

		if (parentDetailAST.getType() == TokenTypes.LITERAL_RETURN) {
			_checkInline(
				parentDetailAST, expressionDetailASTMap, builderClassName);
		}

		if (parentDetailAST.getType() != TokenTypes.ASSIGN) {
			return;
		}

		DetailAST assignDetailAST = parentDetailAST;

		parentDetailAST = assignDetailAST.getParent();

		DetailAST grandParentDetailAST = parentDetailAST.getParent();

		if (grandParentDetailAST.getType() == TokenTypes.OBJBLOCK) {
			DetailAST greatGrandParentDetailAST =
				grandParentDetailAST.getParent();

			if (greatGrandParentDetailAST.getType() == TokenTypes.CLASS_DEF) {
				return;
			}
		}

		_checkInline(parentDetailAST, expressionDetailASTMap, builderClassName);

		if (isJSPFile()) {
			return;
		}

		firstChildDetailAST = assignDetailAST.getFirstChild();

		DetailAST assignValueDetailAST = null;

		if (parentDetailAST.getType() == TokenTypes.EXPR) {
			assignValueDetailAST = firstChildDetailAST.getNextSibling();
		}
		else {
			assignValueDetailAST = firstChildDetailAST.getFirstChild();
		}

		if ((assignValueDetailAST == null) ||
			(assignValueDetailAST.getType() != TokenTypes.METHOD_CALL)) {

			return;
		}

		List<String> variableNames = _getVariableNames(
			parentDetailAST, "get.*");

		String variableName = getVariableName(assignDetailAST, parentDetailAST);

		variableNames.add(variableName);

		String[] builderMethodNames = builderInformation.getMethodNames();

		DetailAST nextSiblingDetailAST = parentDetailAST.getNextSibling();

		while (true) {
			if (nextSiblingDetailAST == null) {
				return;
			}

			FullIdent fullIdent = getMethodCallFullIdent(
				nextSiblingDetailAST, variableName, builderMethodNames);

			if (fullIdent != null) {
				log(
					assignDetailAST, _MSG_INCLUDE_BUILDER, fullIdent.getText(),
					fullIdent.getLineNo(), builderClassName,
					assignDetailAST.getLineNo());

				return;
			}

			for (String curVariableName : variableNames) {
				if (containsVariableName(
						nextSiblingDetailAST, curVariableName)) {

					return;
				}
			}

			nextSiblingDetailAST = nextSiblingDetailAST.getNextSibling();
		}
	}

	private void _checkInline(
		DetailAST parentDetailAST,
		Map<String, List<DetailAST>> expressionDetailASTMap,
		String builderClassName) {

		if (!isAttributeValue(_CHECK_INLINE)) {
			return;
		}

		List<String> followingVariableNames = new ArrayList<>();

		DetailAST nextSiblingDetailAST = parentDetailAST.getNextSibling();

		while (true) {
			if (nextSiblingDetailAST == null) {
				break;
			}

			followingVariableNames.addAll(
				_getVariableNames(nextSiblingDetailAST));

			nextSiblingDetailAST = nextSiblingDetailAST.getNextSibling();
		}

		List<String> inBetweenVariableNames = new ArrayList<>();

		DetailAST previousSiblingDetailAST =
			parentDetailAST.getPreviousSibling();

		while (true) {
			if (previousSiblingDetailAST == null) {
				return;
			}

			if (previousSiblingDetailAST.getType() != TokenTypes.VARIABLE_DEF) {
				followingVariableNames.addAll(
					_getVariableNames(previousSiblingDetailAST));

				inBetweenVariableNames.addAll(
					_getVariableNames(previousSiblingDetailAST, "get.*"));

				previousSiblingDetailAST =
					previousSiblingDetailAST.getPreviousSibling();

				continue;
			}

			DetailAST identDetailAST = previousSiblingDetailAST.findFirstToken(
				TokenTypes.IDENT);

			String name = identDetailAST.getText();

			List<String> supportsFunctionMethodNames =
				getSupportsFunctionMethodNames();

			String matchingMethodName = _getInlineExpressionMethodName(
				expressionDetailASTMap, name);

			if (!followingVariableNames.contains(name) &&
				supportsFunctionMethodNames.contains(matchingMethodName) &&
				!_referencesNonfinalVariable(previousSiblingDetailAST)) {

				List<String> variableNames = _getVariableNames(
					previousSiblingDetailAST);

				boolean contains = false;

				for (String variableName : variableNames) {
					if (inBetweenVariableNames.contains(variableName)) {
						contains = true;

						break;
					}
				}

				if (!contains) {
					log(
						identDetailAST, _MSG_INLINE_BUILDER, name,
						identDetailAST.getLineNo(), builderClassName,
						parentDetailAST.getLineNo());
				}
			}

			followingVariableNames.addAll(
				_getVariableNames(previousSiblingDetailAST));

			inBetweenVariableNames.addAll(
				_getVariableNames(previousSiblingDetailAST, "get.*"));

			previousSiblingDetailAST =
				previousSiblingDetailAST.getPreviousSibling();
		}
	}

	private void _checkNullValues(
		Map<String, List<DetailAST>> expressionDetailASTMap,
		String builderClassName) {

		for (Map.Entry<String, List<DetailAST>> entry :
				expressionDetailASTMap.entrySet()) {

			for (DetailAST expressionDetailAST : entry.getValue()) {
				if (_isNullValueExpression(expressionDetailAST)) {
					log(
						expressionDetailAST, _MSG_INCORRECT_NULL_VALUE,
						builderClassName);
				}
			}
		}
	}

	private BuilderInformation _findBuilderInformationByBuilderClassName(
		String builderClassName) {

		for (BuilderInformation builderInformation :
				getBuilderInformationList()) {

			if (builderClassName.equals(
					builderInformation.getBuilderClassName())) {

				return builderInformation;
			}
		}

		return null;
	}

	private BuilderInformation _findBuilderInformationByClassName(
		String className) {

		if (className == null) {
			return null;
		}

		for (BuilderInformation builderInformation :
				getBuilderInformationList()) {

			if (className.equals(builderInformation.getClassName())) {
				return builderInformation;
			}
		}

		return null;
	}

	private Map<String, List<DetailAST>> _getExpressionDetailASTMap(
		DetailAST methodCallDetailAST) {

		Map<String, List<DetailAST>> expressionDetailASTMap = new HashMap<>();

		while (true) {
			String methodName = getMethodName(methodCallDetailAST);

			List<DetailAST> expressionDetailASTList =
				expressionDetailASTMap.get(methodName);

			if (expressionDetailASTList == null) {
				expressionDetailASTList = new ArrayList<>();
			}

			DetailAST elistDetailAST = methodCallDetailAST.findFirstToken(
				TokenTypes.ELIST);

			DetailAST childDetailAST = elistDetailAST.getFirstChild();

			while (true) {
				if (childDetailAST == null) {
					break;
				}

				if (childDetailAST.getType() != TokenTypes.COMMA) {
					expressionDetailASTList.add(childDetailAST);
				}

				childDetailAST = childDetailAST.getNextSibling();
			}

			if (!expressionDetailASTList.isEmpty()) {
				expressionDetailASTMap.put(methodName, expressionDetailASTList);
			}

			DetailAST parentDetailAST = methodCallDetailAST.getParent();

			if (parentDetailAST.getType() != TokenTypes.DOT) {
				return expressionDetailASTMap;
			}

			methodCallDetailAST = parentDetailAST.getParent();
		}
	}

	private String _getInlineExpressionMethodName(
		Map<String, List<DetailAST>> expressionDetailASTMap, String name) {

		String methodName = null;

		for (Map.Entry<String, List<DetailAST>> entry :
				expressionDetailASTMap.entrySet()) {

			for (DetailAST expressionDetailAST : entry.getValue()) {
				List<String> variableNames = _getVariableNames(
					expressionDetailAST);

				if (variableNames.contains(name)) {
					if (methodName != null) {
						return null;
					}

					methodName = entry.getKey();
				}
			}
		}

		return methodName;
	}

	private List<String> _getVariableNames(DetailAST detailAST) {
		return _getVariableNames(detailAST, null);
	}

	private List<String> _getVariableNames(
		DetailAST detailAST, String excludeRegex) {

		List<String> variableNames = new ArrayList<>();

		List<DetailAST> identDetailASTList = getAllChildTokens(
			detailAST, true, TokenTypes.IDENT);

		for (DetailAST identDetailAST : identDetailASTList) {
			String variableName = identDetailAST.getText();

			if (!variableName.matches("[a-z_].*")) {
				continue;
			}

			DetailAST parentDetailAST = identDetailAST.getParent();

			if ((parentDetailAST.getType() == TokenTypes.ASSIGN) ||
				(parentDetailAST.getType() == TokenTypes.EXPR) ||
				(parentDetailAST.getType() == TokenTypes.TYPECAST) ||
				ArrayUtil.contains(
					ARITHMETIC_OPERATOR_TOKEN_TYPES,
					parentDetailAST.getType()) ||
				ArrayUtil.contains(
					CONDITIONAL_OPERATOR_TOKEN_TYPES,
					parentDetailAST.getType()) ||
				ArrayUtil.contains(
					RELATIONAL_OPERATOR_TOKEN_TYPES,
					parentDetailAST.getType()) ||
				ArrayUtil.contains(
					UNARY_OPERATOR_TOKEN_TYPES, parentDetailAST.getType())) {

				variableNames.add(variableName);
			}
			else if (parentDetailAST.getType() == TokenTypes.DOT) {
				DetailAST nextSiblingDetailAST =
					identDetailAST.getNextSibling();

				if (nextSiblingDetailAST != null) {
					if ((nextSiblingDetailAST.getType() != TokenTypes.IDENT) ||
						(excludeRegex == null)) {

						variableNames.add(variableName);
					}
					else {
						String s = nextSiblingDetailAST.getText();

						if (!s.matches(excludeRegex)) {
							variableNames.add(variableName);
						}
					}
				}
			}
		}

		return variableNames;
	}

	private boolean _isNullValueExpression(DetailAST detailAST) {
		if (detailAST.getType() != TokenTypes.EXPR) {
			return false;
		}

		DetailAST firstChildDetailAST = detailAST.getFirstChild();

		if (firstChildDetailAST.getType() == TokenTypes.LITERAL_NULL) {
			return true;
		}

		if (firstChildDetailAST.getType() == TokenTypes.TYPECAST) {
			DetailAST lastChildDetailAST = firstChildDetailAST.getLastChild();

			if (lastChildDetailAST.getType() == TokenTypes.LITERAL_NULL) {
				return true;
			}
		}

		return false;
	}

	private boolean _referencesNonfinalVariable(DetailAST detailAST) {
		List<String> variableNames = _getVariableNames(detailAST);

		for (String variableName : variableNames) {
			DetailAST variableTypeDetailAST = getVariableTypeDetailAST(
				detailAST, variableName);

			if (variableTypeDetailAST == null) {
				return true;
			}

			DetailAST variableDefinitionDetailAST =
				variableTypeDetailAST.getParent();

			DetailAST parentDetailAST = variableDefinitionDetailAST.getParent();

			if (parentDetailAST.getType() == TokenTypes.OBJBLOCK) {
				DetailAST modifiersDetailAST =
					variableDefinitionDetailAST.findFirstToken(
						TokenTypes.MODIFIERS);

				if ((modifiersDetailAST == null) ||
					!modifiersDetailAST.branchContains(TokenTypes.FINAL)) {

					return true;
				}

				continue;
			}

			List<DetailAST> variableCallerDetailASTList =
				getVariableCallerDetailASTList(
					variableDefinitionDetailAST, variableName);

			for (DetailAST variableCallerDetailAST :
					variableCallerDetailASTList) {

				parentDetailAST = variableCallerDetailAST.getParent();

				if ((parentDetailAST.getType() == TokenTypes.ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.BAND_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.BOR_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.BXOR_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.DEC) ||
					(parentDetailAST.getType() == TokenTypes.DIV_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.INC) ||
					(parentDetailAST.getType() == TokenTypes.MINUS_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.MOD_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.PLUS_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.POST_DEC) ||
					(parentDetailAST.getType() == TokenTypes.POST_INC) ||
					(parentDetailAST.getType() == TokenTypes.SL_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.SR_ASSIGN) ||
					(parentDetailAST.getType() == TokenTypes.STAR_ASSIGN)) {

					return true;
				}
			}
		}

		return false;
	}

	private static final String _CHECK_INLINE = "checkInline";

	private static final String _MSG_INCLUDE_BUILDER = "builder.include";

	private static final String _MSG_INCORRECT_NULL_VALUE =
		"null.value.incorrect";

	private static final String _MSG_INLINE_BUILDER = "builder.inline";

	private static final String _MSG_USE_BUILDER = "builder.use";

	private static final String _MSG_USE_BUILDER_INSTEAD =
		"builder.use.instead";

}