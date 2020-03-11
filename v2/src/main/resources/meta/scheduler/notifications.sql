
-- start  Schema : notifications

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `entity_id` mediumtext COLLATE utf8mb4_unicode_ci,
  `entity_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `communication_type` enum('SMS','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SMS',
  `subject` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'This will be replacing the tags and part of the report! Again jSON blob with attachment file',
  `params` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'The Role Name Cap Poc attachment all will be done in the format of JSON blob',
  `status` enum('OPEN','RUNNING','EXECUTING','EXECUTED','CLOSED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPEN',
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : notifications