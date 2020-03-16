drop table DbEntitySame;
drop table db_entity_not_same;
drop table IntId;

CREATE TABLE DbEntitySame (
    dbEntitySameId varchar2(36) NOT NULL,
    testVarchar varchar2(36) DEFAULT NULL,
    testInt number(11) DEFAULT NULL,
    testBoolean number(1) DEFAULT NULL,
    testEnum varchar2(36) DEFAULT NULL,
    testEnum2 varchar2(36) DEFAULT NULL,
    founderId varchar2(255) DEFAULT NULL,
    founderName varchar2(255) DEFAULT NULL,
    createTime date DEFAULT NULL,
    modifierId varchar2(255) DEFAULT NULL,
    modifierName varchar2(255) DEFAULT NULL,
    modifyTime date DEFAULT NULL,
    deleteFlag number(1) NOT NULL,
    PRIMARY KEY (dbEntitySameId)
);

CREATE TABLE db_entity_not_same (
    db_entity_not_same_id varchar2(36) NOT NULL,
    test_varchar varchar2(36) DEFAULT NULL,
    test_int number(11) DEFAULT NULL,
    test_boolean number(1) DEFAULT NULL,
    founder_id varchar2(255) DEFAULT NULL,
    create_time date DEFAULT NULL,
    modifier_id varchar2(255) DEFAULT NULL,
    modify_time date DEFAULT NULL,
    delete_flag number(1) NOT NULL,
    PRIMARY KEY (db_entity_not_same_id)
);

CREATE TABLE IntId (
    intId number(11) NOT NULL,
    testVarchar varchar2(36) DEFAULT NULL,
    founderId varchar2(255) DEFAULT NULL,
    founderName varchar2(255) DEFAULT NULL,
    createTime date DEFAULT NULL,
    modifierId varchar2(255) DEFAULT NULL,
    modifierName varchar2(255) DEFAULT NULL,
    modifyTime date DEFAULT NULL,
    deleteFlag number(1) NOT NULL,
    PRIMARY KEY (intId)
);
