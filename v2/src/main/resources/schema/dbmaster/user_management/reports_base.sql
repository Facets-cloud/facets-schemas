
-- start  Schema : reports_base

CREATE TABLE `reports_base` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `report_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `report_description` mediumtext COLLATE utf8mb4_unicode_ci,
  `report_code` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : reports_base