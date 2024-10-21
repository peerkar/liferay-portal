/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service.http;

import com.liferay.generative.ai.task.service.TaskDefinitionServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * <code>TaskDefinitionServiceUtil</code> service
 * utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * <code>HttpPrincipal</code> parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class TaskDefinitionServiceHttp {

	public static com.liferay.generative.ai.task.model.TaskDefinition
			addTaskDefinition(
				HttpPrincipal httpPrincipal, String configurationJSON,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String externalReferenceCode, boolean readOnly,
				String schemaVersion,
				com.liferay.portal.kernel.service.ServiceContext serviceContext,
				java.util.Map<java.util.Locale, String> titleMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TaskDefinitionServiceUtil.class, "addTaskDefinition",
				_addTaskDefinitionParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, configurationJSON, descriptionMap,
				externalReferenceCode, readOnly, schemaVersion, serviceContext,
				titleMap);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.generative.ai.task.model.TaskDefinition)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.generative.ai.task.model.TaskDefinition
			deleteTaskDefinition(
				HttpPrincipal httpPrincipal, long taskDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TaskDefinitionServiceUtil.class, "deleteTaskDefinition",
				_deleteTaskDefinitionParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, taskDefinitionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.generative.ai.task.model.TaskDefinition)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.generative.ai.task.model.TaskDefinition
			fetchTaskDefinition(
				HttpPrincipal httpPrincipal, long taskDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TaskDefinitionServiceUtil.class, "fetchTaskDefinition",
				_fetchTaskDefinitionParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, taskDefinitionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.generative.ai.task.model.TaskDefinition)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.generative.ai.task.model.TaskDefinition
			fetchTaskDefinitionByExternalReferenceCode(
				HttpPrincipal httpPrincipal, String externalReferenceCode,
				long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TaskDefinitionServiceUtil.class,
				"fetchTaskDefinitionByExternalReferenceCode",
				_fetchTaskDefinitionByExternalReferenceCodeParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, externalReferenceCode, companyId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.generative.ai.task.model.TaskDefinition)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.generative.ai.task.model.TaskDefinition
			getTaskDefinition(
				HttpPrincipal httpPrincipal, long taskDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TaskDefinitionServiceUtil.class, "getTaskDefinition",
				_getTaskDefinitionParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, taskDefinitionId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.generative.ai.task.model.TaskDefinition)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.generative.ai.task.model.TaskDefinition
			getTaskDefinitionByExternalReferenceCode(
				HttpPrincipal httpPrincipal, long companyId,
				String externalReferenceCode)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TaskDefinitionServiceUtil.class,
				"getTaskDefinitionByExternalReferenceCode",
				_getTaskDefinitionByExternalReferenceCodeParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, companyId, externalReferenceCode);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.generative.ai.task.model.TaskDefinition)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.generative.ai.task.model.TaskDefinition
			updateTaskDefinition(
				HttpPrincipal httpPrincipal, String configurationJSON,
				java.util.Map<java.util.Locale, String> descriptionMap,
				String externalReferenceCode, long taskDefinitionId,
				String schemaVersion,
				com.liferay.portal.kernel.service.ServiceContext serviceContext,
				java.util.Map<java.util.Locale, String> titleMap)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				TaskDefinitionServiceUtil.class, "updateTaskDefinition",
				_updateTaskDefinitionParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, configurationJSON, descriptionMap,
				externalReferenceCode, taskDefinitionId, schemaVersion,
				serviceContext, titleMap);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.generative.ai.task.model.TaskDefinition)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		TaskDefinitionServiceHttp.class);

	private static final Class<?>[] _addTaskDefinitionParameterTypes0 =
		new Class[] {
			String.class, java.util.Map.class, String.class, boolean.class,
			String.class,
			com.liferay.portal.kernel.service.ServiceContext.class,
			java.util.Map.class
		};
	private static final Class<?>[] _deleteTaskDefinitionParameterTypes1 =
		new Class[] {long.class};
	private static final Class<?>[] _fetchTaskDefinitionParameterTypes2 =
		new Class[] {long.class};
	private static final Class<?>[]
		_fetchTaskDefinitionByExternalReferenceCodeParameterTypes3 =
			new Class[] {String.class, long.class};
	private static final Class<?>[] _getTaskDefinitionParameterTypes4 =
		new Class[] {long.class};
	private static final Class<?>[]
		_getTaskDefinitionByExternalReferenceCodeParameterTypes5 = new Class[] {
			long.class, String.class
		};
	private static final Class<?>[] _updateTaskDefinitionParameterTypes6 =
		new Class[] {
			String.class, java.util.Map.class, String.class, long.class,
			String.class,
			com.liferay.portal.kernel.service.ServiceContext.class,
			java.util.Map.class
		};

}