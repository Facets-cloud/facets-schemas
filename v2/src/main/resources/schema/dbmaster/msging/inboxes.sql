
-- start  Schema : inboxes

CREATE TABLE `inboxes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subscriberId` bigint(20) NOT NULL,
  `messageId` int(11) NOT NULL,
  `deliveryAgentId` int(11) NOT NULL,
  `publisherId` bigint(20) NOT NULL,
  `message` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `body` mediumtext COLLATE utf8mb4_unicode_ci,
  `templateArguments` mediumtext COLLATE utf8mb4_unicode_ci,
  `createTime` datetime NOT NULL,
  `processedTime` datetime DEFAULT NULL,
  `retrievedTime` datetime DEFAULT NULL COMMENT 'Time when the message was retrieved for sending to nsadmin',
  `nsadminId` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `subscriberId` (`messageId`,`subscriberId`,`deliveryAgentId`),
  KEY `I_ProcessedTime` (`processedTime`),
  KEY `nsadminId` (`nsadminId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : inboxes