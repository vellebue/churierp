--Users Model
create table USERS(USER_ID int PRIMARY KEY, LOGIN varchar(100) NOT NULL, PASSWORD varchar(512) NOT NULL,
                   NAME varchar(100) NOT NULL, SURNAME VARCHAR(255) NOT NULL, EMAIL varchar(100),
                   CREATION_DATE date NOT NULL, END_DATE date);

create sequence SEQ_USERS
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by USERS.USER_ID;

--addresses model
create table C_COUNTRIES(COUNTRY_ID varchar(2) PRIMARY KEY, NAME varchar(100) NOT NULL, KEY varchar(512) NOT NULL);

create table C_REGIONS(COUNTRY_ID varchar(2), REGION_ID varchar(10),
                       NAME varchar(100) NOT NULL, KEY varchar(512) NOT null,
                       primary key(COUNTRY_ID, REGIONID));

create table ADDRESSES(ADDRESS_ID int PRIMARY KEY, ADDRESS varchar(512) NOT NULL, POSTAL_CODE varchar(15) NOT NULL,
                       CITY varchar(100) NOT NULL, COUNTRY_ID int NOT NULL, REGION_ID int NOT NULL);

create sequence SEQ_ADDRESSES
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by ADDRESSES.ADDRESS_ID;

--Companies model
create table COMPANIES(COMPANY_ID int PRIMARY KEY, NAME varchar(100), SOCIAL_NAME varchar(100), VAT_NUMBER varchar(15));

create sequence SEQ_COMPANIES
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by COMPANIES.COMPANY_ID;