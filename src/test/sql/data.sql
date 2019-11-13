create database if not exists `yt-mybatis`;
use `yt-mybatis`;

CREATE TABLE  if not exists `Message` (
  `messageId` varchar(36) NOT NULL COMMENT 'id',
  `userId` varchar(36) NOT NULL COMMENT '用户id',
  `type` varchar(255) NOT NULL COMMENT '消息类型',
  `content` varchar(255) NOT NULL COMMENT '消息内容',
  `businessId` varchar(36) DEFAULT NULL COMMENT '业务id',
  `read` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `founderId` varchar(255) DEFAULT NULL,
  `founderName` varchar(255) DEFAULT NULL,
  `createDateTime` datetime DEFAULT NULL,
  `modifierId` varchar(255) DEFAULT NULL,
  `modifierName` varchar(255) DEFAULT NULL,
  `modifyDateTime` datetime DEFAULT NULL,
  `deleteFlag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`messageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';


INSERT INTO `Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createDateTime`, `modifierId`, `modifierName`, `modifyDateTime`, `deleteFlag`) VALUES ('message_1', 'user_1_admin', 'INVITE_GROUP', 'null邀请你加入到群组移民和闺蜜中', 'business_1', 1, 'user_2_student', '张三feng', '2019-01-22 05:06:53', 'user_2_student', '张三feng', '2019-01-23 02:19:10', 0);
INSERT INTO `Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createDateTime`, `modifierId`, `modifierName`, `modifyDateTime`, `deleteFlag`) VALUES ('message_2', 'user_1_admin', 'INVITE_GROUP', '杨幂邀请你加入到群组uu中', 'business_1', 1, 'user_2_student', '张三feng', '2019-01-23 05:47:01', 'user_2_student', '张三feng', '2019-01-23 05:47:09', 0);
INSERT INTO `Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createDateTime`, `modifierId`, `modifierName`, `modifyDateTime`, `deleteFlag`) VALUES ('message_3', 'user_2_student', 'INVITE_GROUP', '杨幂邀请你加入到群组uu中', 'business_1', 1, 'user_2_student', '张三feng', '2019-01-23 03:14:26', 'user_2_student', '张三feng', '2019-01-25 03:59:30', 0);
INSERT INTO `Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createDateTime`, `modifierId`, `modifierName`, `modifyDateTime`, `deleteFlag`) VALUES ('message_4', 'user_2_student', 'INVITE_GROUP', 'null邀请你加入到群组xkxkxk中', 'business_1', 1, 'user_2_student', '张三feng', '2019-01-22 05:04:31', 'user_2_student', '张三feng', '2019-01-22 05:05:37', 0);
INSERT INTO `Message`(`messageId`, `userId`, `type`, `content`, `businessId`, `read`, `founderId`, `founderName`, `createDateTime`, `modifierId`, `modifierName`, `modifyDateTime`, `deleteFlag`) VALUES ('message_5', 'user_2_student', 'INVITE_GROUP', '杨幂邀请你加入到群组bnnj中', 'business_1', 0, 'user_2_student', '张三feng', '2019-01-28 21:17:03', NULL, NULL, NULL, 0);
