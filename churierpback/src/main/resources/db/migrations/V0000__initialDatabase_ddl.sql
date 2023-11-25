--Languages model
create table C_LANGUAGES(ID int PRIMARY KEY, LANG_ID VARCHAR(2) NOT NULL, COUNTRY_ID VARCHAR(2) NULL, LANGUAGE_KEY VARCHAR(100) NOT NULL);

--ADMINISTRATION

--Types model

create table C_TYPED_AREAS(ID int PRIMARY KEY, KEY VARCHAR(255) NOT NULL, DESCRIPTION VARCHAR(150) NOT NULL);

create table C_TYPED_ENTITIES(AREA_ID int REFERENCES C_TYPED_AREAS(ID), ID int,
                             JAVA_CLASS VARCHAR(512) NOT NULL, KEY VARCHAR(255) NOT NULL, DESCRIPTION VARCHAR(150) NOT NULL,
                             ALLOW_SUBTYPES BOOLEAN NOT NULL,
                             primary key(AREA_ID,ID));

create table TYPED_TYPES(AREA_ID int, ENTITY_ID int,
                           TYPE_ID VARCHAR(10),
                           KEY VARCHAR(255) NULL, DESCRIPTION VARCHAR(150) NOT NULL, MANAGEABLE BOOLEAN NOT NULL,
                           CONSTRAINT REF_C_TYPED_ENTITIES FOREIGN KEY (AREA_ID, ENTITY_ID) REFERENCES C_TYPED_ENTITIES(AREA_ID, ID),
                           primary key(AREA_ID,ENTITY_ID, TYPE_ID));

create table TYPED_SUBTYPES(AREA_ID int, ENTITY_ID int, TYPE_ID VARCHAR(10), SUBTYPE_ID VARCHAR(10),
                              KEY VARCHAR(255) NULL, DESCRIPTION VARCHAR(150) NOT NULL, MANAGEABLE BOOLEAN NOT NULL,
                              CONSTRAINT REF_TYPED_TYPES FOREIGN KEY (AREA_ID, ENTITY_ID, TYPE_ID) REFERENCES TYPED_TYPES(AREA_ID, ENTITY_ID, TYPE_ID),
                              primary key(AREA_ID,ENTITY_ID, TYPE_ID, SUBTYPE_ID));

--Users Model
create table USERS(USER_ID int PRIMARY KEY,
                   CREATION_USER VARCHAR(100) NOT NULL, CREATION_TIME TIMESTAMP NOT NULL, UPDATE_USER VARCHAR(100) NOT NULL, UPDATE_TIME TIMESTAMP NOT NULL,
                   LOGIN varchar(100) NOT NULL, PASSWORD varchar(512) NOT NULL,
                   NAME varchar(100) NOT NULL, SURNAME VARCHAR(255) NOT NULL, EMAIL varchar(100),
                   CREATION_DATE date NOT NULL, END_DATE date, LANGUAGE_ID int NOT NULL DEFAULT 1 REFERENCES C_LANGUAGES(ID),
                   USER_TYPE VARCHAR(10) NULL, USER_SUBTYPE VARCHAR(10) NULL );

create sequence SEQ_USERS
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by USERS.USER_ID;

--Addresses model
create table C_COUNTRIES(COUNTRY_ID varchar(2) PRIMARY KEY, NAME varchar(100) NOT NULL, KEY varchar(512) NOT NULL);

create table C_REGIONS(COUNTRY_ID varchar(2), REGION_ID varchar(10),
                       NAME varchar(100) NOT NULL, KEY varchar(512) NOT null,
                       primary key(COUNTRY_ID, REGION_ID));

create table C_ADDRESS_TYPES(TYPE_ID varchar(4) PRIMARY KEY, DESCRIPTION varchar(50) NOT NULL, KEY varchar(100) NOT NULL);

create table ADDRESSES(ADDRESS_ID int PRIMARY KEY, TYPE_ID varchar(4) NOT NULL REFERENCES C_ADDRESS_TYPES(TYPE_ID),
                       CREATION_USER VARCHAR(100) NOT NULL, CREATION_TIME TIMESTAMP NOT NULL, UPDATE_USER VARCHAR(100) NOT NULL, UPDATE_TIME TIMESTAMP NOT NULL,
                       ADDRESS varchar(512) NOT NULL, POSTAL_CODE varchar(15) NOT NULL, CITY varchar(100) NOT NULL,
                       COUNTRY_ID varchar(2) NOT NULL REFERENCES C_COUNTRIES(COUNTRY_ID),
                       REGION_ID varchar(10) NOT NULL,
                       constraint fk_addresses_c_regions FOREIGN KEY (COUNTRY_ID, REGION_ID) REFERENCES C_REGIONS(COUNTRY_ID, REGION_ID));

create sequence SEQ_ADDRESSES
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by ADDRESSES.ADDRESS_ID;

--Companies model
create table COMPANIES(COMPANY_ID int PRIMARY KEY, NAME varchar(100), SOCIAL_NAME varchar(100), VAT_NUMBER varchar(15),
                       CREATION_USER VARCHAR(100) NOT NULL, CREATION_TIME TIMESTAMP NOT NULL, UPDATE_USER VARCHAR(100) NOT NULL, UPDATE_TIME TIMESTAMP NOT NULL,
                       ADDRESS_ID INT NOT NULL REFERENCES ADDRESSES(ADDRESS_ID));

create sequence SEQ_COMPANIES
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by COMPANIES.COMPANY_ID;

--ACCOUNTING

--VAT Data Master model
create table VAT_TYPES(COUNTRY_ID varchar(2) NOT NULL REFERENCES C_COUNTRIES(COUNTRY_ID),
                       VAT_ID varchar(2) NOT NULL,
                       CREATION_USER VARCHAR(100) NOT NULL, CREATION_TIME TIMESTAMP NOT NULL, UPDATE_USER VARCHAR(100) NOT NULL, UPDATE_TIME TIMESTAMP NOT NULL,
                       DESCRIPTION varchar(256) NOT NULL,
                       primary key (COUNTRY_ID, VAT_ID));

create table VAT_VALUES(COUNTRY_ID varchar(2) NOT NULL REFERENCES C_COUNTRIES(COUNTRY_ID),
                        VAT_ID varchar(2) NOT NULL,
                        VALID_FROM date NOT NULL, VALID_TO date NULL,
                        CREATION_USER VARCHAR(100) NOT NULL, CREATION_TIME TIMESTAMP NOT NULL, UPDATE_USER VARCHAR(100) NOT NULL, UPDATE_TIME TIMESTAMP NOT NULL,
                        PERCENTAGE DECIMAL(5,2) NOT NULL, UPCHARGE DECIMAL(5,2) NOT NULL,
                        primary key(COUNTRY_ID, VAT_ID, VALID_FROM),
                        constraint fk_vat_values_vat_regions foreign key (COUNTRY_ID, VAT_ID) references VAT_TYPES(COUNTRY_ID, VAT_ID));
--Accounting accounts
create table C_ACCOUNTING_PLANS(PLAN_ID varchar(10) PRIMARY KEY,
                                COUNTRY_ID varchar(2) NOT NULL REFERENCES C_COUNTRIES(COUNTRY_ID),
                                KEY varchar(150) NOT NULL,
                                DESCRIPTION varchar(512) NOT NULL);
create table C_ACCOUNTING_ACCOUNT_KINDS(ID varchar(2) PRIMARY KEY,
                                      KEY varchar(150) NOT NULL,
                                      DESCRIPTION varchar(512) NOT NULL);
create table ACCOUNTING_ACCOUNTS(ACC_ID int PRIMARY KEY,
                                 PLAN_ID varchar(10) NOT NULL REFERENCES C_ACCOUNTING_PLANS(PLAN_ID),
                                 COMPANY_ID int NOT NULL REFERENCES COMPANIES(COMPANY_ID),
                                 CREATION_USER VARCHAR(100) NOT NULL, CREATION_TIME TIMESTAMP NOT NULL, UPDATE_USER VARCHAR(100) NOT NULL, UPDATE_TIME TIMESTAMP NOT NULL,
                                 KIND_ID varchar(2) NOT NULL REFERENCES C_ACCOUNTING_ACCOUNT_KINDS(ID),
                                 DEB_HAB varchar(1) NOT NULL,
                                 ACCOUNT VARCHAR(10) NOT NULL, DESCRIPTION varchar(512) NOT NULL,
                                 TYPE_ID varchar(10) NULL, SUBTYPE_ID varchar(10) NULL,
                                 QUALIFIED BOOLEAN NOT NULL);
create sequence SEQ_AC_ACCOUNTS
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by ACCOUNTING_ACCOUNTS.ACC_ID;