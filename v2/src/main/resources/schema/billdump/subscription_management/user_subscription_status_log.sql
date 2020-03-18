
-- start  Schema : user_subscription_status_log

CREATE TABLE `user_subscription_status_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_subscription_id` int(11) DEFAULT NULL,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `type` enum('OPTIN','OPTOUT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `channel_id` int(11) NOT NULL,
  `priority` enum('BULK','TRANS','ALL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'BULK',
  `subscription_service_id` int(11) DEFAULT NULL,
  `scope_id` int(11) NOT NULL,
  `external_reference_id` int(11) DEFAULT NULL,
  `added_ip` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `added_by_user_source` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by_id` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52394679 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_subscription_status_log