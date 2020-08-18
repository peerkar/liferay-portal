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

package com.liferay.portal.search.tuning.gsearch.impl.internal.parameter.contributor;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.tuning.gsearch.message.Message;
import com.liferay.portal.search.tuning.gsearch.message.Severity;
import com.liferay.portal.search.tuning.gsearch.parameter.BooleanParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.DateParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.IntegerParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.LongArrayParameter;
import com.liferay.portal.search.tuning.gsearch.parameter.ParameterDefinition;
import com.liferay.portal.search.tuning.gsearch.parameter.SearchParameterData;
import com.liferay.portal.search.tuning.gsearch.parameter.StringParameter;
import com.liferay.portal.search.tuning.gsearch.spi.parameter.ParameterContributor;
import com.liferay.segments.context.Context;
import com.liferay.segments.provider.SegmentsEntryProvider;
import com.liferay.segments.simulator.SegmentsEntrySimulator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.LongStream;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, property = "name=user",
	service = ParameterContributor.class
)
public class UserParameterContributor implements ParameterContributor {

	@Override
	public void contribute(
		HttpServletRequest httpServletRequest,
		SearchParameterData searchParameterData) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		_contribute(
			searchParameterData, themeDisplay.getCompanyId(),
			themeDisplay.getUserId());
	}

	@Override
	public void contribute(
		SearchContext searchContext, SearchParameterData searchParameterData) {

		_contribute(
			searchParameterData, searchContext.getCompanyId(),
			searchContext.getUserId());
	}

	public List<ParameterDefinition> getParameterDefinitions() {
		List<ParameterDefinition> parameterDefinitions = new ArrayList<>();

		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.full_name}", StringParameter.class.getName(),
				"parameter.user.full-name"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.first_name}", StringParameter.class.getName(),
				"parameter.user.first-name"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.last_name}", StringParameter.class.getName(),
				"parameter.user.last-name"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.language_id}", StringParameter.class.getName(),
				"parameter.user.language-id"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.job_title}", StringParameter.class.getName(),
				"parameter.user.job-title"));

		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.create_date}", DateParameter.class.getName(),
				"parameter.user.create-date"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.birthday}", DateParameter.class.getName(),
				"parameter.user.birthday"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.age}", IntegerParameter.class.getName(),
				"parameter.user.age"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.is_male}", BooleanParameter.class.getName(),
				"parameter.user.is-male"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.is_female}", BooleanParameter.class.getName(),
				"parameter.user.is-female"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.is_gender_x}", BooleanParameter.class.getName(),
				"parameter.user.is-gender-x"));

		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.email_domain}", StringParameter.class.getName(),
				"parameter.user.email-domain"));

		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.group_ids}", LongArrayParameter.class.getName(),
				"parameter.user.group-ids"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.role_ids}", LongArrayParameter.class.getName(),
				"parameter.user.role-ids"));
		parameterDefinitions.add(
			new ParameterDefinition(
				"${user.segment_entry_ids}", LongArrayParameter.class.getName(),
				"parameter.user.segment-entry-ids"));

		return parameterDefinitions;
	}

	private void _contribute(
		SearchParameterData searchParameterData, long companyId, long userId) {

		User user;

		try {
			user = _userLocalService.getUser(userId);
		}
		catch (PortalException portalException) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.user-not-found",
					portalException.getMessage(), portalException, null, null,
					String.valueOf(userId)));

			_log.error(portalException.getMessage(), portalException);

			return;
		}

		searchParameterData.addParameter(
			new StringParameter(
				"user.full_name", null, "${user.full_name}",
				user.getFullName()));
		searchParameterData.addParameter(
			new StringParameter(
				"user.first_name", null, "${user.first_name}",
				user.getFirstName()));
		searchParameterData.addParameter(
			new StringParameter(
				"user.last_name", null, "${user.last_name}",
				user.getLastName()));
		searchParameterData.addParameter(
			new StringParameter(
				"user.language_id", null, "${user.language_id}",
				user.getLanguageId()));
		searchParameterData.addParameter(
			new StringParameter(
				"user.job_title", null, "${user.job_title}",
				user.getJobTitle()));
		searchParameterData.addParameter(
			new DateParameter(
				"user.create_date", null, "${user.create_date}",
				user.getCreateDate()));

		try {
			searchParameterData.addParameter(
				new DateParameter(
					"user.birthday", null, "${user.birthday}",
					user.getBirthday()));

			searchParameterData.addParameter(
				new IntegerParameter(
					"user.age", null, "${user.age}",
					_getUserAge(user.getBirthday())));
			searchParameterData.addParameter(
				new BooleanParameter(
					"user.is_male", null, "${user.is_male}", user.isMale()));
			searchParameterData.addParameter(
				new BooleanParameter(
					"user.is_female", null, "${user.is_female}",
					user.isFemale()));
			searchParameterData.addParameter(
				new BooleanParameter(
					"user.is_gender_x", null, "${user.is_gender_x}",
					!user.isFemale() && !user.isMale()));
		}
		catch (PortalException portalException) {
			searchParameterData.addMessage(
				new Message(
					Severity.ERROR, "core", "core.error.unknown-exception",
					portalException.getMessage(), portalException, null, null,
					String.valueOf(userId)));
			_log.error(portalException.getMessage(), portalException);
		}

		searchParameterData.addParameter(
			new StringParameter(
				"user.email_domain", null, "${user.email_domain}",
				_getUserEmailDomain(user)));
		searchParameterData.addParameter(
			new LongArrayParameter(
				"user.group_ids", null, "${user.group_ids}",
				LongStream.of(
					user.getGroupIds()
				).boxed(
				).toArray(
					Long[]::new
				)));
		searchParameterData.addParameter(
			new LongArrayParameter(
				"user.role_ids", null, "${user.role_ids}",
				LongStream.of(
					user.getRoleIds()
				).boxed(
				).toArray(
					Long[]::new
				)));

		long[] userGroupIds = _getUserAccessibleSiteGroupIds(companyId, user);

		if (userGroupIds.length > 0) {
			Long[] segmentsEntryIds = _getUserSegmentEntryIds(
				user, userGroupIds);

			searchParameterData.addParameter(
				new LongArrayParameter(
					"user.segment_entry_ids", null, "${user.segment_entry_ids}",
					segmentsEntryIds));
		}
	}

	private long[] _getUserAccessibleSiteGroupIds(long companyId, User user) {
		List<Long> groupIds = new ArrayList<>();

		try {
			Company company = _companyLocalService.getCompany(companyId);

			long companyGroupId = company.getGroupId();
			groupIds.add(companyGroupId);

			for (Group group :
					_groupLocalService.getGroups(companyId, 0, true)) {

				if (group.isActive() && !group.isStagingGroup() &&
					group.hasPublicLayouts()) {

					groupIds.add(group.getGroupId());
				}
			}

			for (Group group : user.getSiteGroups()) {
				if (!groupIds.contains(group.getGroupId()) &&
					group.isActive() && !group.isStagingGroup()) {

					groupIds.add(group.getGroupId());
				}
			}
		}
		catch (PortalException portalException) {
			_log.error(portalException.getMessage(), portalException);
		}

		groupIds.toArray(new Long[0]);

		return groupIds.stream(
		).mapToLong(
			l -> l
		).toArray();
	}

	private int _getUserAge(Date birthday) {
		Date now = new Date();

		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		int d1 = Integer.parseInt(formatter.format(birthday));
		int d2 = Integer.parseInt(formatter.format(now));
		int age = (d2 - d1) / 10000;

		return age;
	}

	private String _getUserEmailDomain(User user) {
		String email = user.getEmailAddress();

		return email.substring(email.indexOf("@") + 1);
	}

	private Long[] _getUserSegmentEntryIds(User user, long[] groupIds) {
		if ((_segmentsEntrySimulator != null) &&
			_segmentsEntrySimulator.isSimulationActive(user.getUserId())) {

			long[] simulatedSegmentsEntryIds =
				_segmentsEntrySimulator.getSimulatedSegmentsEntryIds(
					user.getUserId());

			return LongStream.of(
				simulatedSegmentsEntryIds
			).boxed(
			).toArray(
				Long[]::new
			);
		}

		try {
			List<Long> allSegmentEntryIds = new ArrayList<>();

			Context context = new Context();

			for (long groupId : groupIds) {
				long[] segmentEntryIds =
					_segmentsEntryProvider.getSegmentsEntryIds(
						groupId, User.class.getName(), user.getUserId(),
						context);

				Arrays.stream(
					segmentEntryIds
				).forEach(
					allSegmentEntryIds::add
				);
			}

			return allSegmentEntryIds.toArray(new Long[0]);
		}
		catch (PortalException portalException) {
			_log.error(portalException.getMessage(), portalException);
		}

		return new Long[0];
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserParameterContributor.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private SegmentsEntryProvider _segmentsEntryProvider;

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(model.class.name=com.liferay.portal.kernel.model.User)"
	)
	private volatile SegmentsEntrySimulator _segmentsEntrySimulator;

	@Reference
	private UserLocalService _userLocalService;

}