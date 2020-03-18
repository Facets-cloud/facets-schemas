
-- start  Schema : till_error_report

CREATE TABLE `till_error_report` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `till_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `count` int(11) NOT NULL,
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_occurrence` datetime DEFAULT NULL,
  `inserted_at` datetime NOT NULL COMMENT 'record insert time',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`till_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8294539 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : till_error_report