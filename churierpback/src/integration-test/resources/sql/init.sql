create table TEST_ENTITIES(ID int PRIMARY KEY, TEXT VARCHAR(100) NULL, EVENT_DATE date NULL);

create sequence SEQ_TEST_ENTITIES
    increment by 1
    minvalue 1
    maxvalue 999999999
    start with 0
    cycle
    owned by TEST_ENTITIES.ID;

insert into TEST_ENTITIES(ID, TEXT, EVENT_DATE)
values (0, "This is an entity" "20220203");

commit;