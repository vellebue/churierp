create table TEST_ENTITIES(ID int PRIMARY KEY, TEXT VARCHAR(100) NULL, EVENT_DATE date NULL);

create sequence SEQ_TEST_ENTITIES
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by TEST_ENTITIES.ID;

insert into TEST_ENTITIES(ID, TEXT, EVENT_DATE)
values (nextval('SEQ_TEST_ENTITIES'), 'This is an entity', to_date('2022/02/03', 'yyyy/MM/dd'));
insert into TEST_ENTITIES(ID, TEXT, EVENT_DATE)
values (nextval('SEQ_TEST_ENTITIES'), 'This is the second entity', to_date('2022/03/01', 'yyyy/MM/dd'));
insert into TEST_ENTITIES(ID, TEXT, EVENT_DATE)
values (nextval('SEQ_TEST_ENTITIES'), 'This is an entity to test fromDtoToEntity', to_date('2022/03/02', 'yyyy/MM/dd'));


commit;