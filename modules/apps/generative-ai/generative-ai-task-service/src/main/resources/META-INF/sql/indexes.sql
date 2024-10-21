create unique index IX_AFEC7333 on TaskDefinition (companyId, externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_5B3A3F20 on TaskDefinition (companyId, readOnly);
create index IX_62663D56 on TaskDefinition (uuid_[$COLUMN_LENGTH:75$]);