
-- start  Schema : incoming_interaction_action_log

CREATE TABLE `incoming_interaction_action_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `interaction_id` bigint(20) NOT NULL,
  `mapping_id` bigint(20) NOT NULL,
  `action_id` bigint(20) NOT NULL,
  `api_success` tinyint(4) NOT NULL,
  `api_item_success` tinyint(4) NOT NULL,
  `api_item_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `api_item_message` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` datetime NOT NULL,
  `added_by` bigint(20) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `interaction_id_mapping_id_action_id_idx` (`interaction_id`,`mapping_id`,`action_id`),
  KEY `org_id_action_id_idx` (`org_id`,`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : incoming_interaction_action_log