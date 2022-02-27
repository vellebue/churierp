--Users Model
create table USERS(USER_ID int PRIMARY KEY, LOGIN varchar(100) NOT NULL, PASSWORD varchar(512) NOT NULL,
                   NAME varchar(100) NOT NULL, SURNAME VARCHAR(255) NOT NULL,
                   CREATION_DATE date NOT NULL, END_DATE date);

create sequence SEQ_USERS
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by USERS.USER_ID;