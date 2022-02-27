--Test table
create table TEST1(ID int PRIMARY KEY, CREATION_DATE date, TEXT VARCHAR(255));

create sequence SEQ_TEST1
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by TEST1.ID;