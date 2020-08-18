/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.gsearch.configuration.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the remote service interface for SearchConfiguration. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see SearchConfigurationServiceUtil
 * @generated
 */
@AccessControlled
@JSONWebService
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface SearchConfigurationService extends BaseService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link SearchConfigurationServiceUtil} to access the search configuration remote service. Add custom service methods to <code>com.liferay.portal.search.tuning.gsearch.configuration.service.impl.SearchConfigurationServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public SearchConfiguration addCompanySearchConfiguration(
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			String configuration, int type, ServiceContext serviceContext)
		throws PortalException;

	public SearchConfiguration addGroupSearchConfiguration(
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			String configuration, int type, ServiceContext serviceContext)
		throws PortalException;

	public SearchConfiguration deleteSearchConfiguration(
			long searchConfigurationId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SearchConfiguration> getGroupSearchConfigurations(
		long companyId, int type, int start, int end);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SearchConfiguration> getGroupSearchConfigurations(
		long companyId, int status, int type, int start, int end);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SearchConfiguration> getGroupSearchConfigurations(
		long companyId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SearchConfiguration> getGroupSearchConfigurations(
		long companyId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getGroupSearchConfigurationsCount(long companyId, int type);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getGroupSearchConfigurationsCount(
		long companyId, int status, int type);

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SearchConfiguration getSearchConfiguration(
			long searchConfigurationId)
		throws PortalException;

	public SearchConfiguration updateSearchConfiguration(
			long searchConfigurationId, Map<Locale, String> titleMap,
			Map<Locale, String> descriptionMap, String configuration,
			ServiceContext serviceContext)
		throws PortalException;

}