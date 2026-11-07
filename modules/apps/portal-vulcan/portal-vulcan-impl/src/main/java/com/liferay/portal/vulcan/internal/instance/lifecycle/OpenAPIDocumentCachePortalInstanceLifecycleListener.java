/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.vulcan.internal.instance.lifecycle;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.EveryNodeEveryStartup;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.NamedThreadFactory;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.vulcan.application.HeadlessApplicationProvider;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.runtime.JaxrsServiceRuntime;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Petteri Karttunen
 */
@Component(service = PortalInstanceLifecycleListener.class)
public class OpenAPIDocumentCachePortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener
	implements EveryNodeEveryStartup {

	@Override
	public void portalInstanceRegistered(Company company) {
		_schedule();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
			new NamedThreadFactory(
				OpenAPIDocumentCachePortalInstanceLifecycleListener.class.
					getName(),
				Thread.MIN_PRIORITY, null));

		_jaxrsServiceRuntimeServiceTracker = ServiceTrackerFactory.open(
			bundleContext, JaxrsServiceRuntime.class,
			new ServiceTrackerCustomizer<>() {

				@Override
				public ServiceReference<JaxrsServiceRuntime> addingService(
					ServiceReference<JaxrsServiceRuntime> serviceReference) {

					_schedule();

					return serviceReference;
				}

				@Override
				public void modifiedService(
					ServiceReference<JaxrsServiceRuntime> serviceReference,
					ServiceReference<JaxrsServiceRuntime>
						trackedServiceReference) {

					_schedule();
				}

				@Override
				public void removedService(
					ServiceReference<JaxrsServiceRuntime> serviceReference,
					ServiceReference<JaxrsServiceRuntime>
						trackedServiceReference) {
				}

			});
	}

	@Deactivate
	protected void deactivate() {
		_jaxrsServiceRuntimeServiceTracker.close();

		_scheduledExecutorService.shutdownNow();
	}

	private User _getUser(long companyId) throws PortalException {
		Role role = _roleLocalService.fetchRole(
			companyId, RoleConstants.ADMINISTRATOR);

		if (role == null) {
			return _userLocalService.getGuestUser(companyId);
		}

		List<User> users = _userLocalService.getRoleUsers(
			role.getRoleId(), 0, 1);

		if (users.isEmpty()) {
			return _userLocalService.getGuestUser(companyId);
		}

		return users.get(0);
	}

	private void _populateOpenAPIDocumentCache() {
		_jaxrsServiceRuntimeServiceTracker.close();

		try {
			_companyLocalService.forEachCompanyId(
				this::_populateOpenAPIDocumentCache);
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	private void _populateOpenAPIDocumentCache(long companyId)
		throws PortalException {

		long startTime = System.currentTimeMillis();

		Company company = _companyLocalService.getCompany(companyId);

		String serverURL =
			company.getPortalURL(0) + _portal.getPathContext() +
				Portal.PATH_MODULE;

		User user = _getUser(companyId);

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();
		String name = PrincipalThreadLocal.getName();

		int count = 0;

		try {
			PermissionThreadLocal.setPermissionChecker(
				_permissionCheckerFactory.create(user));
			PrincipalThreadLocal.setName(user.getUserId());

			for (HeadlessApplicationProvider.Application application :
					_headlessApplicationProvider.getApplications(companyId)) {

				for (HeadlessApplicationProvider.OpenAPIDocument
						openAPIDocument : application.getOpenAPIDocuments()) {

					openAPIDocument.getContentString(
						serverURL,
						HeadlessApplicationProvider.OpenAPIDocument.Type.JSON);

					count++;
				}
			}
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(permissionChecker);
			PrincipalThreadLocal.setName(name);
		}

		if (_log.isInfoEnabled()) {
			_log.info(
				StringBundler.concat(
					"Populated the OpenAPI document cache with ", count,
					" documents of company ", companyId, " in ",
					System.currentTimeMillis() - startTime, " ms"));
		}
	}

	private synchronized void _schedule() {
		if (_scheduledExecutorService.isShutdown()) {
			return;
		}

		if (_scheduledFuture != null) {
			_scheduledFuture.cancel(false);
		}

		_scheduledFuture = _scheduledExecutorService.schedule(
			() -> _populateOpenAPIDocumentCache(), 30, TimeUnit.SECONDS);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenAPIDocumentCachePortalInstanceLifecycleListener.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private HeadlessApplicationProvider _headlessApplicationProvider;

	private ServiceTracker
		<JaxrsServiceRuntime, ServiceReference<JaxrsServiceRuntime>>
			_jaxrsServiceRuntimeServiceTracker;

	@Reference
	private PermissionCheckerFactory _permissionCheckerFactory;

	@Reference
	private Portal _portal;

	@Reference
	private RoleLocalService _roleLocalService;

	private ScheduledExecutorService _scheduledExecutorService;
	private ScheduledFuture<?> _scheduledFuture;

	@Reference
	private UserLocalService _userLocalService;

}