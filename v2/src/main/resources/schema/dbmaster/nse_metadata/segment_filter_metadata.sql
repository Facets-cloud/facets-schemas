
-- start  Schema : segment_filter_metadata

CREATE TABLE `segment_filter_metadata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  `segment_id` bigint(20) NOT NULL,
  `rule` text COLLATE utf8mb4_unicode_ci,
  `valid_on` datetime DEFAULT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `segment_index` (`id`,`segment_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : segment_filter_metadata