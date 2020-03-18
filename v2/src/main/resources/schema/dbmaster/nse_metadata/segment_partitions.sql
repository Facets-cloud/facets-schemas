
-- start  Schema : segment_partitions

CREATE TABLE `segment_partitions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `segment_id` bigint(20) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8 COMMENT '	',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `name` (`org_id`,`segment_id`,`name`),
  KEY `org_segment` (`org_id`,`segment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : segment_partitions