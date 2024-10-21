/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.task;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Fabian Bouché
 */
@ProviderType
public interface TaskMemoryCleaner {
    
    public void clear(String memoryId);

}
