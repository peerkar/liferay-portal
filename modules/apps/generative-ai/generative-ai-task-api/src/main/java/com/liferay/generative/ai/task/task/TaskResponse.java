/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.task;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Petteri KArttunen
 */
@ProviderType
public interface TaskResponse {

	public Map<String, Object> getDebugInfo();

	public Map<String, Object> getOutput();

}