
-- start  Schema : credit_notes

CREATE TABLE `credit_notes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `user_id` int(11) DEFAULT NULL,
  `number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_type` enum('RETURNED','LOYALTY_LOG') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_id` int(11) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `notes` longtext COLLATE utf8mb4_unicode_ci,
  `validity` timestamp NULL DEFAULT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `entered_by` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`),
  KEY `org_user_time` (`org_id`,`user_id`,`added_on`),
  KEY `org_entered_by_date` (`org_id`,`entered_by`,`added_on`),
  KEY `org_ref` (`org_id`,`reference_type`,`reference_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : credit_notes