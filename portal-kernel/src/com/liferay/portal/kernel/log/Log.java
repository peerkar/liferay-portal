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

package com.liferay.portal.kernel.log;

/**
 * @author Brian Wing Shun Chan
 */
public interface Log {

	public void debug(Object msg);

	public void debug(Object msg, Throwable throwable);

	public void debug(Throwable throwable);

	public void error(Object msg);

	public void error(Object msg, Throwable throwable);

	public void error(Throwable throwable);

	public void fatal(Object msg);

	public void fatal(Object msg, Throwable throwable);

	public void fatal(Throwable throwable);

	public void info(Object msg);

	public void info(Object msg, Throwable throwable);

	public void info(Throwable throwable);

	public boolean isDebugEnabled();

	public boolean isErrorEnabled();

	public boolean isFatalEnabled();

	public boolean isInfoEnabled();

	public boolean isTraceEnabled();

	public boolean isWarnEnabled();

	public void setLogWrapperClassName(String className);

	public void trace(Object msg);

	public void trace(Object msg, Throwable throwable);

	public void trace(Throwable throwable);

	public void warn(Object msg);

	public void warn(Object msg, Throwable throwable);

	public void warn(Throwable throwable);

}