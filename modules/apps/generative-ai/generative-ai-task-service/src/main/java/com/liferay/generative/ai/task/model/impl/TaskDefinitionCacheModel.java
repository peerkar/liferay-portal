/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.model.impl;

import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing TaskDefinition in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class TaskDefinitionCacheModel
	implements CacheModel<TaskDefinition>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof TaskDefinitionCacheModel)) {
			return false;
		}

		TaskDefinitionCacheModel taskDefinitionCacheModel =
			(TaskDefinitionCacheModel)object;

		if ((taskDefinitionId == taskDefinitionCacheModel.taskDefinitionId) &&
			(mvccVersion == taskDefinitionCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, taskDefinitionId);

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
		StringBundler sb = new StringBundler(39);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", externalReferenceCode=");
		sb.append(externalReferenceCode);
		sb.append(", taskDefinitionId=");
		sb.append(taskDefinitionId);
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
		sb.append(", configurationJSON=");
		sb.append(configurationJSON);
		sb.append(", description=");
		sb.append(description);
		sb.append(", readOnly=");
		sb.append(readOnly);
		sb.append(", schemaVersion=");
		sb.append(schemaVersion);
		sb.append(", title=");
		sb.append(title);
		sb.append(", version=");
		sb.append(version);
		sb.append(", status=");
		sb.append(status);
		sb.append(", statusByUserId=");
		sb.append(statusByUserId);
		sb.append(", statusByUserName=");
		sb.append(statusByUserName);
		sb.append(", statusDate=");
		sb.append(statusDate);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public TaskDefinition toEntityModel() {
		TaskDefinitionImpl taskDefinitionImpl = new TaskDefinitionImpl();

		taskDefinitionImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			taskDefinitionImpl.setUuid("");
		}
		else {
			taskDefinitionImpl.setUuid(uuid);
		}

		if (externalReferenceCode == null) {
			taskDefinitionImpl.setExternalReferenceCode("");
		}
		else {
			taskDefinitionImpl.setExternalReferenceCode(externalReferenceCode);
		}

		taskDefinitionImpl.setTaskDefinitionId(taskDefinitionId);
		taskDefinitionImpl.setCompanyId(companyId);
		taskDefinitionImpl.setUserId(userId);

		if (userName == null) {
			taskDefinitionImpl.setUserName("");
		}
		else {
			taskDefinitionImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			taskDefinitionImpl.setCreateDate(null);
		}
		else {
			taskDefinitionImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			taskDefinitionImpl.setModifiedDate(null);
		}
		else {
			taskDefinitionImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (configurationJSON == null) {
			taskDefinitionImpl.setConfigurationJSON("");
		}
		else {
			taskDefinitionImpl.setConfigurationJSON(configurationJSON);
		}

		if (description == null) {
			taskDefinitionImpl.setDescription("");
		}
		else {
			taskDefinitionImpl.setDescription(description);
		}

		taskDefinitionImpl.setReadOnly(readOnly);

		if (schemaVersion == null) {
			taskDefinitionImpl.setSchemaVersion("");
		}
		else {
			taskDefinitionImpl.setSchemaVersion(schemaVersion);
		}

		if (title == null) {
			taskDefinitionImpl.setTitle("");
		}
		else {
			taskDefinitionImpl.setTitle(title);
		}

		if (version == null) {
			taskDefinitionImpl.setVersion("");
		}
		else {
			taskDefinitionImpl.setVersion(version);
		}

		taskDefinitionImpl.setStatus(status);
		taskDefinitionImpl.setStatusByUserId(statusByUserId);

		if (statusByUserName == null) {
			taskDefinitionImpl.setStatusByUserName("");
		}
		else {
			taskDefinitionImpl.setStatusByUserName(statusByUserName);
		}

		if (statusDate == Long.MIN_VALUE) {
			taskDefinitionImpl.setStatusDate(null);
		}
		else {
			taskDefinitionImpl.setStatusDate(new Date(statusDate));
		}

		taskDefinitionImpl.resetOriginalValues();

		return taskDefinitionImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput)
		throws ClassNotFoundException, IOException {

		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();
		externalReferenceCode = objectInput.readUTF();

		taskDefinitionId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		configurationJSON = (String)objectInput.readObject();
		description = objectInput.readUTF();

		readOnly = objectInput.readBoolean();
		schemaVersion = objectInput.readUTF();
		title = objectInput.readUTF();
		version = objectInput.readUTF();

		status = objectInput.readInt();

		statusByUserId = objectInput.readLong();
		statusByUserName = objectInput.readUTF();
		statusDate = objectInput.readLong();
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

		if (externalReferenceCode == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(externalReferenceCode);
		}

		objectOutput.writeLong(taskDefinitionId);

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

		if (configurationJSON == null) {
			objectOutput.writeObject("");
		}
		else {
			objectOutput.writeObject(configurationJSON);
		}

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		objectOutput.writeBoolean(readOnly);

		if (schemaVersion == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(schemaVersion);
		}

		if (title == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(title);
		}

		if (version == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(version);
		}

		objectOutput.writeInt(status);

		objectOutput.writeLong(statusByUserId);

		if (statusByUserName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(statusByUserName);
		}

		objectOutput.writeLong(statusDate);
	}

	public long mvccVersion;
	public String uuid;
	public String externalReferenceCode;
	public long taskDefinitionId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String configurationJSON;
	public String description;
	public boolean readOnly;
	public String schemaVersion;
	public String title;
	public String version;
	public int status;
	public long statusByUserId;
	public String statusByUserName;
	public long statusDate;

}