
-- start  Schema : outboxes

CREATE TABLE `outboxes` (
  `messageId` int(11) NOT NULL AUTO_INCREMENT,
  `guid` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` enum('EMAIL','SMS','STORE_TASK','WECHAT','PUSH','ANDROID','IOS','FACEBOOK','GOOGLE','TWITTER','LINE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `publisherId` int(11) NOT NULL,
  `messageText` longtext COLLATE utf8mb4_unicode_ci,
  `body` longtext COLLATE utf8mb4_unicode_ci,
  `recepientType` enum('subscriber','callback','listed','filtered','grouped','dummy','timeline','social') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recepientList` longtext COLLATE utf8mb4_unicode_ci COMMENT 'List of recepients in comma separated form (recepient IDs)',
  `categoryIds` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'List of category Ids this message is meant for',
  `sendTime` datetime DEFAULT NULL COMMENT 'Time the message should be sent',
  `sendTaskId` int(11) DEFAULT NULL COMMENT 'Scheduler Task ID For this msg',
  `status` enum('opened','processing','paused','closed') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'opened',
  `numDeliveries` mediumint(9) NOT NULL DEFAULT '0',
  `defaultArguments` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'JSON serialized K-V Pairs',
  `createdTime` datetime NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`messageId`),
  KEY `status` (`status`),
  KEY `guid` (`guid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : outboxes