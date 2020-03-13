drop table DbEntitySame;
drop table db_entity_not_same;
drop table IntId;


CREATE TABLE DbEntitySame (
    dbEntitySameId varchar(500) NOT NULL,
    testVarchar varchar(500) DEFAULT NULL,
    testInt int DEFAULT NULL,
    testBoolean boolean DEFAULT NULL,
    testEnum varchar(500) DEFAULT NULL,
    testEnum2 varchar(500) DEFAULT NULL,
    founderId varchar(500) DEFAULT NULL,
    founderName varchar(500) DEFAULT NULL,
    createTime TIMESTAMP DEFAULT NULL,
    modifierId varchar(500) DEFAULT NULL,
    modifierName varchar(500) DEFAULT NULL,
    modifyTime TIMESTAMP DEFAULT NULL,
    deleteFlag boolean NOT NULL,
    PRIMARY KEY (dbEntitySameId)
);

CREATE TABLE db_entity_not_same (
    db_entity_not_same_id varchar(500) NOT NULL,
    test_varchar varchar(500) DEFAULT NULL,
    test_int int DEFAULT NULL,
    test_boolean boolean DEFAULT NULL,
    founder_id varchar(500) DEFAULT NULL,
    create_time TIMESTAMP DEFAULT NULL,
    modifier_id varchar(500) DEFAULT NULL,
    modify_time TIMESTAMP DEFAULT NULL,
    delete_flag boolean NOT NULL,
    PRIMARY KEY (db_entity_not_same_id)
);

CREATE TABLE IntId (
    intId int NOT NULL,
    testVarchar varchar(500) DEFAULT NULL,
    founderId varchar(500) DEFAULT NULL,
    founderName varchar(500) DEFAULT NULL,
    createTime TIMESTAMP DEFAULT NULL,
    modifierId varchar(500) DEFAULT NULL,
    modifierName varchar(500) DEFAULT NULL,
    modifyTime TIMESTAMP DEFAULT NULL,
    deleteFlag boolean NOT NULL,
    PRIMARY KEY (intId)
);
