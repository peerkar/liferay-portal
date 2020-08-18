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

package com.liferay.portal.search.tuning.gsearch.impl.internal.query.contributor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.query.TermQuery;
import com.liferay.portal.search.tuning.gsearch.configuration.constants.json.values.Occur;
import com.liferay.portal.search.tuning.gsearch.context.SearchRequestContext;
import com.liferay.portal.search.tuning.gsearch.exception.SearchRequestDataException;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.spi.query.QueryContributor;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = QueryContributor.class)
public class PermissionPreFilterContributor implements QueryContributor {

	@Override
	public Optional<Query> build(SearchRequestContext searchRequestContext)
		throws SearchRequestDataException {

		long companyId = searchRequestContext.getCompanyId();
		long userId = searchRequestContext.getUserId();

		try {
			User user = _userLocalService.getUser(userId);

			if (_portal.isCompanyAdmin(user)) {
				return Optional.empty();
			}

			BooleanQuery permissionQuery = _queries.booleanQuery();

			Role guestRole = _roleLocalService.getRole(
				companyId, RoleConstants.GUEST);

			if (userId != 0) {
				TermQuery guestTermQuery = _queries.term(
					Field.ROLE_ID, guestRole.getRoleId());

				permissionQuery.addShouldQueryClauses(guestTermQuery);
			}

			TermQuery ownerTermQuery = _queries.term(Field.USER_ID, userId);

			permissionQuery.addShouldQueryClauses(ownerTermQuery);

			_addRegularRoleClauses(permissionQuery, userId);

			_addGroupRoleClauses(permissionQuery, companyId, user);

			return Optional.of(permissionQuery);
		}
		catch (Exception exception) {
			searchRequestContext.addMessage(
				new Message(
					Severity.ERROR, "core",
					"core.error.could-not-add-view-permission-filter-clauses",
					exception.getMessage(), exception, null, null, null));
			_log.error(exception.getMessage(), exception);
		}

		return Optional.empty();
	}

	@Override
	public Occur getOccur() {
		return Occur.MUST;
	}

	private void _addGroupRoleClauses(
			BooleanQuery permissionQuery, long companyId, User user)
		throws PortalException {

		Role siteMemberRole = _roleLocalService.getRole(
			companyId, RoleConstants.SITE_MEMBER);

		for (Group group : user.getSiteGroups()) {
			TermQuery termQuery = _queries.term(
				Field.GROUP_ROLE_ID,
				group.getGroupId() + "-" + siteMemberRole.getRoleId());

			permissionQuery.addShouldQueryClauses(termQuery);

			for (Role role :
					_roleLocalService.getUserGroupRoles(
						user.getUserId(), group.getGroupId())) {

				TermQuery groupTermQuery = _queries.term(
					Field.GROUP_ROLE_ID,
					group.getGroupId() + "-" + role.getRoleId());

				permissionQuery.addShouldQueryClauses(groupTermQuery);
			}
		}
	}

	private void _addRegularRoleClauses(
		BooleanQuery permissionQuery, long userId) {

		for (Role role : _roleLocalService.getUserRoles(userId)) {
			TermQuery termQuery = _queries.term(
				Field.ROLE_ID, role.getRoleId());

			permissionQuery.addShouldQueryClauses(termQuery);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PermissionPreFilterContributor.class);

	@Reference
	private Portal _portal;

	@Reference
	private Queries _queries;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}