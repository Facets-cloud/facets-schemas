
-- start  Schema : segment_upload_metadata

CREATE TABLE `segment_upload_metadata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  `segment_id` bigint(20) NOT NULL,
  `external_source` text COLLATE utf8mb4_unicode_ci,
  `upload_identifier` enum('user_id','mobile','email','external_id','customer_external_id') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `valid_on` datetime DEFAULT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `segment_index` (`id`,`segment_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : segment_upload_metadata