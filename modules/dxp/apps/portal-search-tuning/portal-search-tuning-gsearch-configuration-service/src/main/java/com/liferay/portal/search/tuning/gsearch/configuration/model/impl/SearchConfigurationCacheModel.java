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

package com.liferay.portal.search.tuning.gsearch.configuration.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing SearchConfiguration in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class SearchConfigurationCacheModel
	implements CacheModel<SearchConfiguration>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SearchConfigurationCacheModel)) {
			return false;
		}

		SearchConfigurationCacheModel searchConfigurationCacheModel =
			(SearchConfigurationCacheModel)obj;

		if ((searchConfigurationId ==
				searchConfigurationCacheModel.searchConfigurationId) &&
			(mvccVersion == searchConfigurationCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, searchConfigurationId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(35);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", searchConfigurationId=");
		sb.append(searchConfigurationId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", status=");
		sb.append(status);
		sb.append(", statusByUserId=");
		sb.append(statusByUserId);
		sb.append(", statusByUserName=");
		sb.append(statusByUserName);
		sb.append(", statusDate=");
		sb.append(statusDate);
		sb.append(", title=");
		sb.append(title);
		sb.append(", description=");
		sb.append(description);
		sb.append(", configuration=");
		sb.append(configuration);
		sb.append(", type=");
		sb.append(type);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public SearchConfiguration toEntityModel() {
		SearchConfigurationImpl searchConfigurationImpl =
			new SearchConfigurationImpl();

		searchConfigurationImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			searchConfigurationImpl.setUuid("");
		}
		else {
			searchConfigurationImpl.setUuid(uuid);
		}

		searchConfigurationImpl.setSearchConfigurationId(searchConfigurationId);
		searchConfigurationImpl.setGroupId(groupId);
		searchConfigurationImpl.setCompanyId(companyId);
		searchConfigurationImpl.setUserId(userId);

		if (userName == null) {
			searchConfigurationImpl.setUserName("");
		}
		else {
			searchConfigurationImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			searchConfigurationImpl.setCreateDate(null);
		}
		else {
			searchConfigurationImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			searchConfigurationImpl.setModifiedDate(null);
		}
		else {
			searchConfigurationImpl.setModifiedDate(new Date(modifiedDate));
		}

		searchConfigurationImpl.setStatus(status);
		searchConfigurationImpl.setStatusByUserId(statusByUserId);

		if (statusByUserName == null) {
			searchConfigurationImpl.setStatusByUserName("");
		}
		else {
			searchConfigurationImpl.setStatusByUserName(statusByUserName);
		}

		if (statusDate == Long.MIN_VALUE) {
			searchConfigurationImpl.setStatusDate(null);
		}
		else {
			searchConfigurationImpl.setStatusDate(new Date(statusDate));
		}

		if (title == null) {
			searchConfigurationImpl.setTitle("");
		}
		else {
			searchConfigurationImpl.setTitle(title);
		}

		if (description == null) {
			searchConfigurationImpl.setDescription("");
		}
		else {
			searchConfigurationImpl.setDescription(description);
		}

		if (configuration == null) {
			searchConfigurationImpl.setConfiguration("");
		}
		else {
			searchConfigurationImpl.setConfiguration(configuration);
		}

		searchConfigurationImpl.setType(type);

		searchConfigurationImpl.resetOriginalValues();

		return searchConfigurationImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput)
		throws ClassNotFoundException, IOException {

		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();

		searchConfigurationId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		status = objectInput.readInt();

		statusByUserId = objectInput.readLong();
		statusByUserName = objectInput.readUTF();
		statusDate = objectInput.readLong();
		title = objectInput.readUTF();
		description = objectInput.readUTF();
		configuration = (String)objectInput.readObject();

		type = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(searchConfigurationId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeInt(status);

		objectOutput.writeLong(statusByUserId);

		if (statusByUserName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(statusByUserName);
		}

		objectOutput.writeLong(statusDate);

		if (title == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(title);
		}

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		if (configuration == null) {
			objectOutput.writeObject("");
		}
		else {
			objectOutput.writeObject(configuration);
		}

		objectOutput.writeInt(type);
	}

	public long mvccVersion;
	public String uuid;
	public long searchConfigurationId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public int status;
	public long statusByUserId;
	public String statusByUserName;
	public long statusDate;
	public String title;
	public String description;
	public String configuration;
	public int type;

}