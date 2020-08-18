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

package com.liferay.portal.log;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogWrapper;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Brian Wing Shun Chan
 */
public class Log4jLogImpl implements Log {

	public Log4jLogImpl(Logger logger) {
		_logger = logger;
	}

	@Override
	public void debug(Object msg) {
		_logger.log(_logWrapperClassName, Level.DEBUG, msg, null);
	}

	@Override
	public void debug(Object msg, Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.DEBUG, msg, throwable);
	}

	@Override
	public void debug(Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.DEBUG, null, throwable);
	}

	@Override
	public void error(Object msg) {
		_logger.log(_logWrapperClassName, Level.ERROR, msg, null);
	}

	@Override
	public void error(Object msg, Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.ERROR, msg, throwable);
	}

	@Override
	public void error(Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.ERROR, null, throwable);
	}

	@Override
	public void fatal(Object msg) {
		_logger.log(_logWrapperClassName, Level.FATAL, msg, null);
	}

	@Override
	public void fatal(Object msg, Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.FATAL, msg, throwable);
	}

	@Override
	public void fatal(Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.FATAL, null, throwable);
	}

	@Override
	public void info(Object msg) {
		_logger.log(_logWrapperClassName, Level.INFO, msg, null);
	}

	@Override
	public void info(Object msg, Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.INFO, msg, throwable);
	}

	@Override
	public void info(Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.INFO, null, throwable);
	}

	@Override
	public boolean isDebugEnabled() {
		return _logger.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return _logger.isEnabledFor(Level.ERROR);
	}

	@Override
	public boolean isFatalEnabled() {
		return _logger.isEnabledFor(Level.FATAL);
	}

	@Override
	public boolean isInfoEnabled() {
		return _logger.isInfoEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return _logger.isTraceEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return _logger.isEnabledFor(Level.WARN);
	}

	@Override
	public void setLogWrapperClassName(String className) {
		_logWrapperClassName = className;
	}

	@Override
	public void trace(Object msg) {
		_logger.log(_logWrapperClassName, Level.TRACE, msg, null);
	}

	@Override
	public void trace(Object msg, Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.TRACE, msg, throwable);
	}

	@Override
	public void trace(Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.TRACE, null, throwable);
	}

	@Override
	public void warn(Object msg) {
		_logger.log(_logWrapperClassName, Level.WARN, msg, null);
	}

	@Override
	public void warn(Object msg, Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.WARN, msg, throwable);
	}

	@Override
	public void warn(Throwable throwable) {
		_logger.log(_logWrapperClassName, Level.WARN, null, throwable);
	}

	private final Logger _logger;
	private String _logWrapperClassName = LogWrapper.class.getName();

}