create table TEST_ENTITIES(ID int PRIMARY KEY, TEXT VARCHAR(100) NULL, EVENT_DATE date NULL);

create sequence SEQ_TEST_ENTITIES
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by TEST_ENTITIES.ID;