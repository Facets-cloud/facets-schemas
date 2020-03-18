
-- start  Schema : namespaces

CREATE TABLE `namespaces` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bucket` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cdn_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s3_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(4) DEFAULT '1',
  `storage_type` enum('S3','AZUREBLOB') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'S3',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_idx` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : namespaces