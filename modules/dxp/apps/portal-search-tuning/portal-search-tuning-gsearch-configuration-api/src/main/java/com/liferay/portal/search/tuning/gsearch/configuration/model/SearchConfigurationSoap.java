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

package com.liferay.portal.search.tuning.gsearch.configuration.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.portal.search.tuning.gsearch.configuration.service.http.SearchConfigurationServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class SearchConfigurationSoap implements Serializable {

	public static SearchConfigurationSoap toSoapModel(
		SearchConfiguration model) {

		SearchConfigurationSoap soapModel = new SearchConfigurationSoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setUuid(model.getUuid());
		soapModel.setSearchConfigurationId(model.getSearchConfigurationId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setStatus(model.getStatus());
		soapModel.setStatusByUserId(model.getStatusByUserId());
		soapModel.setStatusByUserName(model.getStatusByUserName());
		soapModel.setStatusDate(model.getStatusDate());
		soapModel.setTitle(model.getTitle());
		soapModel.setDescription(model.getDescription());
		soapModel.setConfiguration(model.getConfiguration());
		soapModel.setType(model.getType());

		return soapModel;
	}

	public static SearchConfigurationSoap[] toSoapModels(
		SearchConfiguration[] models) {

		SearchConfigurationSoap[] soapModels =
			new SearchConfigurationSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static SearchConfigurationSoap[][] toSoapModels(
		SearchConfiguration[][] models) {

		SearchConfigurationSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new SearchConfigurationSoap[models.length][models[0].length];
		}
		else {
			soapModels = new SearchConfigurationSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static SearchConfigurationSoap[] toSoapModels(
		List<SearchConfiguration> models) {

		List<SearchConfigurationSoap> soapModels =
			new ArrayList<SearchConfigurationSoap>(models.size());

		for (SearchConfiguration model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(
			new SearchConfigurationSoap[soapModels.size()]);
	}

	public SearchConfigurationSoap() {
	}

	public long getPrimaryKey() {
		return _searchConfigurationId;
	}

	public void setPrimaryKey(long pk) {
		setSearchConfigurationId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getSearchConfigurationId() {
		return _searchConfigurationId;
	}

	public void setSearchConfigurationId(long searchConfigurationId) {
		_searchConfigurationId = searchConfigurationId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}

	public long getStatusByUserId() {
		return _statusByUserId;
	}

	public void setStatusByUserId(long statusByUserId) {
		_statusByUserId = statusByUserId;
	}

	public String getStatusByUserName() {
		return _statusByUserName;
	}

	public void setStatusByUserName(String statusByUserName) {
		_statusByUserName = statusByUserName;
	}

	public Date getStatusDate() {
		return _statusDate;
	}

	public void setStatusDate(Date statusDate) {
		_statusDate = statusDate;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getConfiguration() {
		return _configuration;
	}

	public void setConfiguration(String configuration) {
		_configuration = configuration;
	}

	public int getType() {
		return _type;
	}

	public void setType(int type) {
		_type = type;
	}

	private long _mvccVersion;
	private String _uuid;
	private long _searchConfigurationId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private int _status;
	private long _statusByUserId;
	private String _statusByUserName;
	private Date _statusDate;
	private String _title;
	private String _description;
	private String _configuration;
	private int _type;

}