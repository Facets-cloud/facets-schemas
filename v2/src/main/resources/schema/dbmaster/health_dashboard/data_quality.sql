
-- start  Schema : data_quality

CREATE TABLE `data_quality` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `store_id` int(20) NOT NULL,
  `type` enum('LOGGED_IN','SIGNUP','TRANS_SUMMARY','TRANS_DATA') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `json_values` mediumtext COLLATE utf8mb4_unicode_ci,
  `calculated_on` date DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_type_calculatedon_idx` (`org_id`,`type`,`calculated_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : data_quality