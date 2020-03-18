
-- start  Schema : user_subscription_status

CREATE TABLE `user_subscription_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `type` enum('OPTIN','OPTOUT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `channel_id` int(11) NOT NULL,
  `priority` enum('BULK','TRANS','ALL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ALL',
  `subscription_service_id` int(11) DEFAULT NULL,
  `scope_id` int(11) NOT NULL,
  `external_reference_id` int(11) DEFAULT NULL,
  `added_ip` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_by_user_source` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by_id` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `auto_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `oucpe` (`org_id`,`user_id`,`channel_id`,`priority`,`external_reference_id`),
  KEY `external_ref_idx` (`external_reference_id`),
  KEY `auto_update_idx` (`org_id`,`auto_update_time`),
  KEY `auto_update_time_index` (`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=67976373 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_subscription_status