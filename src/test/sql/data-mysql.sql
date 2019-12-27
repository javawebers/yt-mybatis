create database if not exists `yt-mybatis`;
use `yt-mybatis`;

-- 表名和实体名一致
CREATE TABLE  if not exists `DbEntitySame` (
  `dbEntitySameId` varchar(36) NOT NULL COMMENT 'id',
  `testVarchar` varchar(36) DEFAULT NULL COMMENT 'String类型',
  `testInt` int(11) DEFAULT NULL COMMENT 'int类型',
  `testBoolean` tinyint(1) DEFAULT NULL COMMENT 'boolean类型',
  `testEnum` enum('MALE','FEMALE') DEFAULT NULL COMMENT 'enum 类型，MALE:男 ，FEMALE:女',
  `testEnum2` enum('MALE','FEMALE','OTHER') DEFAULT NULL COMMENT 'enum 类型，MALE:男 ，FEMALE:女，OTHER:其他',
  `founderId` varchar(255) DEFAULT NULL,
  `founderName` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `modifierId` varchar(255) DEFAULT NULL,
  `modifierName` varchar(255) DEFAULT NULL,
  `modifyTime` datetime DEFAULT NULL,
  `deleteFlag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`dbEntitySameId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表名和实体名一致';

-- 表名和实体名一致
CREATE TABLE  if not exists `db_entity_not_same` (
  `db_entity_not_same_id` varchar(36) NOT NULL COMMENT 'id',
  `test_varchar` varchar(36) DEFAULT NULL COMMENT 'String类型',
  `test_int` int(11) DEFAULT NULL COMMENT 'int类型',
  `test_boolean` tinyint(1) DEFAULT NULL COMMENT 'boolean类型',
  `founder_id` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modifier_id` varchar(255) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`db_entity_not_same_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表名和实体名不一致(驼峰)';


-- 表名和实体名一致
CREATE TABLE  if not exists `IntId` (
  `intId` int(11) NOT NULL COMMENT 'id',
  `testVarchar` varchar(36) DEFAULT NULL COMMENT 'String类型',
  `founderId` varchar(255) DEFAULT NULL,
  `founderName` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `modifierId` varchar(255) DEFAULT NULL,
  `modifierName` varchar(255) DEFAULT NULL,
  `modifyTime` datetime DEFAULT NULL,
  `deleteFlag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`intId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='int类型id';
