
-- start  Schema : store_units

CREATE TABLE `store_units` (
  `id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `client_type` enum('TILL','STR_SERVER') COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `client_version_num` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `compile_time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `svn_revision` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `established_on` date NOT NULL,
  `mac_addr` varchar(17) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'mac address of the counter',
  `store_server_prefix` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'store server prefix',
  `disable_mac_addr_check` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Disable checking of mac address',
  `is_weblogin_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `last_login` datetime NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `zone_id` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_billable` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`),
  KEY `store_id` (`store_id`),
  KEY `parent_id` (`parent_id`),
  KEY `time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Information about the stores';


-- end  Schema : store_units