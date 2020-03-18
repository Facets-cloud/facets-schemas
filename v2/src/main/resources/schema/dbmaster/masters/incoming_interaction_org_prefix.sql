
-- start  Schema : incoming_interaction_org_prefix

CREATE TABLE `incoming_interaction_org_prefix` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `org_id` int(11) NOT NULL COMMENT 'Foreign Key Referring To `organizations`.`id`',
  `prefix` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`),
  UNIQUE KEY `prefix` (`prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : incoming_interaction_org_prefix