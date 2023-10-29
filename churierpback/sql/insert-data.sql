--Languages model
insert into C_LANGUAGES(ID, LANG_ID, COUNTRY_ID, LANGUAGE_KEY) values
(0, 'en', null, 'language.en'),
(1, 'es', 'ES', 'language.es.ES');

--Users Model

--Areas, typed entities, types and subtypes
insert into C_TYPED_AREAS(ID, KEY, DESCRIPTION)
values(0, 'churierpweb.administration.areas.administration' , 'Administration');
insert into C_TYPED_AREAS(ID, KEY, DESCRIPTION)
values(1, 'churierpweb.administration.areas.accounting' , 'Accounting');
insert into C_TYPED_AREAS(ID, KEY, DESCRIPTION)
values(2, 'churierpweb.administration.areas.sales' , 'Sales');

insert into C_TYPED_ENTITIES(AREA_ID, ID, JAVA_CLASS, KEY, DESCRIPTION, ALLOW_SUBTYPES)
values(0, 0, 'org.bastanchu.churierp.churierpback.entity.administration.users.User',
       'churierpweb.administration.typedentities.user', 'User', true);
insert into C_TYPED_ENTITIES(AREA_ID, ID, JAVA_CLASS, KEY, DESCRIPTION, ALLOW_SUBTYPES)
values(0, 1, 'org.bastanchu.churierp.churierpback.entity.administration.companies.Company',
       'churierpweb.administration.typedentities.company', 'Company', true);
insert into C_TYPED_ENTITIES(AREA_ID, ID, JAVA_CLASS, KEY, DESCRIPTION, ALLOW_SUBTYPES)
values(1, 2, 'org.bastanchu.churierp.churierpback.entity.accounting.accounts.Account',
       'churierpweb.accounting.typedentities.accounting', 'Account', true);

insert into TYPED_TYPES(AREA_ID, ENTITY_ID, TYPE_ID, KEY, DESCRIPTION, MANAGEABLE)
values(0, 0, 'REG_USER', 'churierpweb.administration.types.user.reg_user', 'Regular User', false);
insert into TYPED_TYPES(AREA_ID, ENTITY_ID, TYPE_ID, KEY, DESCRIPTION, MANAGEABLE)
values(0, 0, 'SYS_USER', 'churierpweb.administration.types.user.sys_user', 'System User', false);
insert into TYPED_TYPES(AREA_ID, ENTITY_ID, TYPE_ID, KEY, DESCRIPTION, MANAGEABLE)
values(0, 1, 'REG_COMP', 'churierpweb.administration.types.company.reg_company', 'Regular company', false);

insert into TYPED_SUBTYPES(AREA_ID, ENTITY_ID, TYPE_ID, SUBTYPE_ID, KEY, DESCRIPTION, MANAGEABLE)
values(0, 0, 'REG_USER', 'STDREGUSER',
       'churierpweb.administration.subtypes.user.sys_user.std_reg_user',
       'Default regular user', false);
insert into TYPED_SUBTYPES(AREA_ID, ENTITY_ID, TYPE_ID, SUBTYPE_ID, KEY, DESCRIPTION, MANAGEABLE)
values(0, 0, 'SYS_USER', 'STDSYSUSER',
       'churierpweb.administration.subtypes.user.sys_user.std_sys_user',
       'Default system user', false);
insert into TYPED_SUBTYPES(AREA_ID, ENTITY_ID, TYPE_ID, SUBTYPE_ID, KEY, DESCRIPTION, MANAGEABLE)
values(0, 1, 'REG_COMP', 'DEF_REG_CO',
       'churierpweb.administration.subtypes.company.reg_company.def_reg_company',
       'Default regular company', false);

--User login angel passwd angel8
select nextval('seq_users');
INSERT INTO users (user_id, creation_user, creation_time, update_user, update_time, login,"password","name",surname,email,creation_date,end_date, language_id, user_type, user_subtype) VALUES
	 (0, 'root', CURRENT_TIMESTAMP, 'root', CURRENT_TIMESTAMP, 'angel','{YjFGd5N2gfpbcucFL2J3uJjfIp8BgT+oMBl1i7wv6kM=}9fbf567e3dcc32e6905c465fbc63f9ccc57fe305d14f7c9a55458126baba1803',
	  'Ángel','García Bastanchuri','vellebue@gmail.com','2022-07-17',NULL, 1, 'REG_USER', 'STDREGUSER');

--Addresses model
INSERT INTO c_address_types (type_id,description,"key") VALUES
	 ('COMP', 'Company Address', 'churierpweb.address.type.comp');

--Accounting accounts
INSERT INTO C_ACCOUNTING_PLANS(PLAN_ID, COUNTRY_ID, KEY, DESCRIPTION)
VALUES ('NPGC', 'ES', 'churierpweb.accounting.accounts.plan.npgc', 'Nuevo Plan General Contable Spain');

INSERT INTO C_ACCOUNTING_ACCOUNT_KINDS(ID, KEY, DESCRIPTION)
VALUES ('X', 'churierpweb.accounting.accounts.accountkind.X', 'Balance Account'),
       ('P', 'churierpweb.accounting.accounts.accountkind.P', 'P&L Account'),
       ('A', 'churierpweb.accounting.accounts.accountkind.A', 'Analytical Account');

commit;