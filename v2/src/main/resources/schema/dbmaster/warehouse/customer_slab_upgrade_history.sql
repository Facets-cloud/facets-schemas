
-- start  Schema : customer_slab_upgrade_history

CREATE TABLE `customer_slab_upgrade_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `program_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL COMMENT 'customer id for whom the slab upgrade was done',
  `event_type_id` int(11) NOT NULL COMMENT 'event under which slab upgrade happened',
  `source_type_id` int(11) NOT NULL COMMENT 'reference to the points source types table which caused the upgrade',
  `source_id` bigint(20) NOT NULL COMMENT 'reference to the source which is responsible for the upgrade',
  `from_slab_id` int(11) NOT NULL COMMENT 'slab in which the customer was present in before the upgrade',
  `to_slab_id` int(11) NOT NULL COMMENT 'slab to which the customer was upgraded to',
  `slab_upgrade_strategy_id` int(11) DEFAULT NULL COMMENT 'the strategy which was used to upgrade the slab',
  `slab_upgrade_start_rule` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'The start rule identifier through which the slab upgrade would have happened',
  `till_id` int(11) NOT NULL COMMENT 'till in context',
  `upgraded_on` datetime NOT NULL COMMENT 'when the upgrade happened',
  `notes` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'information about the slab upgrade',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`org_id`,`program_id`,`customer_id`,`upgraded_on`,`from_slab_id`,`to_slab_id`) USING BTREE,
  KEY `auto_time_idx` (`upgraded_on`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : customer_slab_upgrade_history