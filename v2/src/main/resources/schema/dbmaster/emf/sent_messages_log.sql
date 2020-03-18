
-- start  Schema : sent_messages_log

CREATE TABLE `sent_messages_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `transaction_id` bigint(20) NOT NULL,
  `nsadmin_id` bigint(20) NOT NULL,
  `type` enum('SMS','EMAIL','WECHAT','MOBILEPUSH') COLLATE utf8mb4_unicode_ci NOT NULL,
  `receiver` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endpoint_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_type_id` int(3) NOT NULL,
  `ruleset_id` int(11) NOT NULL,
  `rule_id` int(11) NOT NULL,
  `rule_case_id` int(11) NOT NULL,
  `till_id` int(11) NOT NULL,
  `sent_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_update_time` (`auto_update_time`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `customer_nsadmin_index` (`org_id`,`customer_id`,`nsadmin_id`) USING BTREE,
  KEY `nsadmin_index` (`org_id`,`nsadmin_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : sent_messages_log