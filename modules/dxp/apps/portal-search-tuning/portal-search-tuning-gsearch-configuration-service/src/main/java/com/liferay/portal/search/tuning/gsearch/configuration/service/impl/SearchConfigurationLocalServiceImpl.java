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

package com.liferay.portal.search.tuning.gsearch.configuration.service.impl;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.service.base.SearchConfigurationLocalServiceBaseImpl;
import com.liferay.portal.search.tuning.gsearch.configuration.validation.SearchConfigurationValidator;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the search configuration local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.search.tuning.gsearch.configuration.service.SearchConfigurationLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SearchConfigurationLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration",
	service = AopService.class
)
public class SearchConfigurationLocalServiceImpl
	extends SearchConfigurationLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SearchConfiguration addSearchConfiguration(
			long userId, long groupId, Map<Locale, String> titleMap,
			Map<Locale, String> descriptionMap, String configuration, int type,
			ServiceContext serviceContext)
		throws PortalException {

		User user = _userLocalService.getUser(userId);

		_searchConfigurationValidator.validate(
			titleMap, descriptionMap, configuration);

		long searchConfigurationId = counterLocalService.increment(
			SearchConfiguration.class.getName());

		SearchConfiguration searchConfiguration = createSearchConfiguration(
			searchConfigurationId);

		searchConfiguration.setCompanyId(user.getCompanyId());
		searchConfiguration.setCreateDate(
			serviceContext.getCreateDate(new Date()));
		searchConfiguration.setGroupId(groupId);
		searchConfiguration.setModifiedDate(
			serviceContext.getModifiedDate(new Date()));
		searchConfiguration.setUserId(user.getUserId());
		searchConfiguration.setUserName(user.getFullName());
		searchConfiguration.setUuid(serviceContext.getUuid());

		searchConfiguration.setTitleMap(titleMap);
		searchConfiguration.setDescriptionMap(descriptionMap);

		int status = WorkflowConstants.STATUS_DRAFT;

		searchConfiguration.setStatus(status);

		searchConfiguration.setStatusByUserId(userId);
		searchConfiguration.setStatusDate(serviceContext.getModifiedDate(null));

		searchConfiguration.setConfiguration(configuration);
		searchConfiguration.setType(type);

		searchConfiguration = super.addSearchConfiguration(searchConfiguration);

		_resourceLocalService.addModelResources(
			searchConfiguration, serviceContext);

		return startWorkflowInstance(
			userId, searchConfiguration, serviceContext);
	}

	@Override
	public SearchConfiguration deleteSearchConfiguration(
			long searchConfigurationId)
		throws PortalException {

		SearchConfiguration searchConfiguration =
			searchConfigurationPersistence.findByPrimaryKey(
				searchConfigurationId);

		return deleteSearchConfiguration(searchConfiguration);
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public SearchConfiguration deleteSearchConfiguration(
			SearchConfiguration searchConfiguration)
		throws PortalException {

		_resourceLocalService.deleteResource(
			searchConfiguration, ResourceConstants.SCOPE_INDIVIDUAL);

		workflowInstanceLinkLocalService.deleteWorkflowInstanceLinks(
			searchConfiguration.getCompanyId(),
			searchConfiguration.getGroupId(),
			SearchConfiguration.class.getName(),
			searchConfiguration.getSearchConfigurationId());

		return super.deleteSearchConfiguration(searchConfiguration);
	}

	public List<SearchConfiguration> getGroupSearchConfigurations(
		long groupId, int type, int start, int end) {

		return getGroupSearchConfigurations(
			groupId, WorkflowConstants.STATUS_APPROVED, type, start, end);
	}

	public List<SearchConfiguration> getGroupSearchConfigurations(
		long groupId, int status, int type, int start, int end) {

		if (status == WorkflowConstants.STATUS_ANY) {
			return searchConfigurationPersistence.findByG_T(
				groupId, type, start, end);
		}

		return searchConfigurationPersistence.findByG_S_T(
			groupId, status, type, start, end);
	}

	public List<SearchConfiguration> getGroupSearchConfigurations(
		long groupId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		if (status == WorkflowConstants.STATUS_ANY) {
			return searchConfigurationPersistence.findByG_T(
				groupId, type, start, end, orderByComparator);
		}

		return searchConfigurationPersistence.findByG_S_T(
			groupId, status, type, start, end, orderByComparator);
	}

	public List<SearchConfiguration> getGroupSearchConfigurations(
		long groupId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return getGroupSearchConfigurations(
			groupId, WorkflowConstants.STATUS_APPROVED, type, start, end,
			orderByComparator);
	}

	public int getGroupSearchConfigurationsCount(long groupId, int type) {
		return getGroupSearchConfigurationsCount(
			groupId, WorkflowConstants.STATUS_APPROVED, type);
	}

	public int getGroupSearchConfigurationsCount(
		long groupId, int status, int type) {

		return searchConfigurationPersistence.countByG_S_T(
			groupId, status, type);
	}

	@Indexable(type = IndexableType.REINDEX)
	public SearchConfiguration updateSearchConfiguration(
			long userId, long searchConfigurationId,
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			String configuration, ServiceContext serviceContext)
		throws PortalException {

		SearchConfiguration searchConfiguration = getSearchConfiguration(
			searchConfigurationId);

		_searchConfigurationValidator.validate(
			titleMap, descriptionMap, configuration);

		searchConfiguration.setDescriptionMap(descriptionMap);
		searchConfiguration.setModifiedDate(
			serviceContext.getModifiedDate(new Date()));
		searchConfiguration.setTitleMap(titleMap);

		searchConfiguration.setConfiguration(configuration);

		return updateSearchConfiguration(searchConfiguration);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SearchConfiguration updateStatus(
			long userId, long searchConfigurationId, int status)
		throws PortalException {

		User user = _userLocalService.getUser(userId);
		SearchConfiguration searchConfiguration = getSearchConfiguration(
			searchConfigurationId);

		searchConfiguration.setStatus(status);
		searchConfiguration.setStatusByUserId(userId);
		searchConfiguration.setStatusByUserName(user.getScreenName());
		searchConfiguration.setStatusDate(new Date());

		return updateSearchConfiguration(searchConfiguration);
	}

	protected SearchConfiguration startWorkflowInstance(
			long userId, SearchConfiguration searchConfiguration,
			ServiceContext serviceContext)
		throws PortalException {

		String userPortraitURL = StringPool.BLANK;
		String userURL = StringPool.BLANK;

		if (serviceContext.getThemeDisplay() != null) {
			User user = _userLocalService.getUser(userId);

			userPortraitURL = user.getPortraitURL(
				serviceContext.getThemeDisplay());
			userURL = user.getDisplayURL(serviceContext.getThemeDisplay());
		}

		Map<String, Serializable> workflowContext =
			HashMapBuilder.<String, Serializable>put(
				WorkflowConstants.CONTEXT_USER_PORTRAIT_URL, userPortraitURL
			).put(
				WorkflowConstants.CONTEXT_USER_URL, userURL
			).build();

		return WorkflowHandlerRegistryUtil.startWorkflowInstance(
			searchConfiguration.getCompanyId(),
			searchConfiguration.getGroupId(), userId,
			SearchConfiguration.class.getName(),
			searchConfiguration.getSearchConfigurationId(), searchConfiguration,
			serviceContext, workflowContext);
	}

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private SearchConfigurationValidator _searchConfigurationValidator;

	@Reference
	private UserLocalService _userLocalService;

}