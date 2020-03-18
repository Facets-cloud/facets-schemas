
-- start  Schema : loyalty_log_outliers

CREATE TABLE `loyalty_log_outliers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loyalty_id` bigint(20) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `bill_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `points` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `notes` mediumtext COLLATE utf8mb4_unicode_ci,
  `bill_amount` float NOT NULL,
  `entered_by` bigint(20) NOT NULL,
  `outlier_status` enum('MARK_DONE','OUTLIER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OUTLIER',
  `xml` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`bill_number`),
  KEY `loyalty_id` (`loyalty_id`),
  KEY `org_id_2` (`org_id`,`user_id`),
  KEY `org_id_3` (`org_id`,`date`),
  KEY `org_id_4` (`org_id`,`entered_by`),
  KEY `date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loyalty_log_outliers