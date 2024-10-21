/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.rest.client.dto.v1_0;

import com.liferay.generative.ai.rest.client.function.UnsafeSupplier;
import com.liferay.generative.ai.rest.client.serdes.v1_0.GenerativeAIResponseSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Petteri Karttunen
 * @generated
 */
@Generated("")
public class GenerativeAIResponse implements Cloneable, Serializable {

	public static GenerativeAIResponse toDTO(String json) {
		return GenerativeAIResponseSerDes.toDTO(json);
	}

	public Object getDebugInfo() {
		return debugInfo;
	}

	public void setDebugInfo(Object debugInfo) {
		this.debugInfo = debugInfo;
	}

	public void setDebugInfo(
		UnsafeSupplier<Object, Exception> debugInfoUnsafeSupplier) {

		try {
			debugInfo = debugInfoUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Object debugInfo;

	public Object getOutput() {
		return output;
	}

	public void setOutput(Object output) {
		this.output = output;
	}

	public void setOutput(
		UnsafeSupplier<Object, Exception> outputUnsafeSupplier) {

		try {
			output = outputUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Object output;

	public String getTook() {
		return took;
	}

	public void setTook(String took) {
		this.took = took;
	}

	public void setTook(UnsafeSupplier<String, Exception> tookUnsafeSupplier) {
		try {
			took = tookUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String took;

	@Override
	public GenerativeAIResponse clone() throws CloneNotSupportedException {
		return (GenerativeAIResponse)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof GenerativeAIResponse)) {
			return false;
		}

		GenerativeAIResponse generativeAIResponse =
			(GenerativeAIResponse)object;

		return Objects.equals(toString(), generativeAIResponse.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return GenerativeAIResponseSerDes.toJSON(this);
	}

}