
-- start  Schema : jobs

CREATE TABLE `jobs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `segment_id` bigint(20) NOT NULL,
  `type` enum('UPLOAD_SEGMENT','PERSIST_SEGMENT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('OPEN','IN_PROGRESS','SUCCESS','ERROR','ACCEPT','DISCARD') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `segment_index` (`id`,`segment_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : jobs