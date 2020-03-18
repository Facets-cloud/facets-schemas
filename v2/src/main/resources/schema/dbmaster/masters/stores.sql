
-- start  Schema : stores

CREATE TABLE `stores` (
  `id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `city_id` int(11) NOT NULL,
  `area_id` int(11) DEFAULT NULL,
  `mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `land_line` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `lat` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `long` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `external_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `external_id_1` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `external_id_2` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `time_zone_offset` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '+05:30' COMMENT 'Adding this column for backward compatibility',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `channels` enum('instore','e-comm','newsletter','campaigns','NCA','WECHAT','MARTJACK','WEB_ENGAGE','FACEBOOK','TMALL','OTHERS','kiosk','aggregator') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'instore',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`),
  KEY `city_id` (`city_id`),
  KEY `org_external_sid_idx` (`org_id`,`external_id`,`id`),
  KEY `time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : stores