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

package com.liferay.portal.odata.sort;

import javax.ws.rs.BadRequestException;

/**
 * Models {@link Sort} errors.
 *
 * @author Cristina González
 * @review
 */
public class InvalidSortException extends BadRequestException {

	/**
	 * Creates a new {@code InvalidSortException} with the provided message.
	 *
	 * @param  msg the message
	 * @review
	 */
	public InvalidSortException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new {@code InvalidSortException} with the provided message and
	 * throwable.
	 *
	 * @param  msg the message
	 * @param  throwable the throwable
	 * @review
	 */
	public InvalidSortException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}