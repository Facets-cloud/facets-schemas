
-- start  Schema : merchant_info

CREATE TABLE `merchant_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `source_id` bigint(20) NOT NULL DEFAULT '0',
  `till_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_type` enum('MERCHANT_DEFAULT','CHANNEL','LOCATION') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MERCHANT_DEFAULT',
  `store_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : merchant_info