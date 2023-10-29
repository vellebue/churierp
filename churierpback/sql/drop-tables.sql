--Accounting accounts
drop sequence SEQ_AC_ACCOUNTS;
drop table ACCOUNTING_ACCOUNTS;
drop table C_ACCOUNTING_ACCOUNT_KINDS;
drop table C_ACCOUNTING_PLANS;

--VAT Data Master model
drop table VAT_VALUES;
drop table VAT_TYPES;

--Companies model
drop sequence SEQ_COMPANIES;
drop table COMPANIES;

--Addresses model
drop sequence SEQ_ADDRESSES;
drop table ADDRESSES;
drop table C_ADDRESS_TYPES;
drop table C_REGIONS;
drop table C_COUNTRIES;

--Users Model
drop sequence SEQ_USERS;
drop table USERS;

--Types Model
drop table TYPED_SUBTYPES;
drop table TYPED_TYPES;
drop table C_TYPED_ENTITIES;
drop table C_TYPED_AREAS;

--Languages Model
drop table C_LANGUAGES;