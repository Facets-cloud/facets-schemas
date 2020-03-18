
-- start  Schema : message_adset_mapping

CREATE TABLE `message_adset_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `message_queue_id` int(11) NOT NULL,
  `adset_id` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `adset_name` mediumtext COLLATE utf8mb4_unicode_ci,
  `last_updated_on` timestamp NULL DEFAULT NULL,
  `last_updated_by` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_id_message_queue_id` (`org_id`,`message_queue_id`,`is_active`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : message_adset_mapping