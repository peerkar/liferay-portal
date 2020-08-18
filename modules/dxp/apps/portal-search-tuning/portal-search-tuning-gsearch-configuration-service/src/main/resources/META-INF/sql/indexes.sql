create index IX_F12F125B on SearchConfiguration (companyId, status, type_);
create index IX_901D901A on SearchConfiguration (companyId, title[$COLUMN_LENGTH:500$], status);
create index IX_389FAC01 on SearchConfiguration (companyId, type_);
create index IX_8E141299 on SearchConfiguration (groupId, status, type_);
create index IX_2D029058 on SearchConfiguration (groupId, title[$COLUMN_LENGTH:500$], status);
create index IX_37EFCA3F on SearchConfiguration (groupId, type_);
create index IX_FEBD4548 on SearchConfiguration (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_97E67DCA on SearchConfiguration (uuid_[$COLUMN_LENGTH:75$], groupId);

create index IX_D6B5DEA2 on SearchConfigurationSnippet (companyId, type_);