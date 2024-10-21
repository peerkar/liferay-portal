/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.internal.util;

import com.liferay.portal.kernel.util.Validator;

import java.util.function.Consumer;

/**
 * @author Petteri Karttunen
 */
public class SetterUtil {

	public static void setNotBlankString(
		Consumer<String> consumer, String value) {

		if (!Validator.isBlank(value)) {
			consumer.accept(value);
		}
	}

	public static void setNotNullDoubleAsFloat(
		Consumer<Float> consumer, Double value) {

		if (value != null) {
			consumer.accept(value.floatValue());
		}
	}

	public static void setNotNullInteger(
		Consumer<Integer> consumer, Integer value) {

		if (value != null) {
			consumer.accept(value);
		}
	}

	public static void setNotNullLong(Consumer<Long> consumer, Long value) {
		if (value != null) {
			consumer.accept(value);
		}
	}

}