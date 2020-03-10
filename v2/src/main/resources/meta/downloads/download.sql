
-- start  Schema : download

CREATE TABLE `download` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `status` enum('OPEN','EXECUTED','PROCESSING','ERROR','ARCHIVED') COLLATE utf8mb4_unicode_ci DEFAULT 'OPEN',
  `module` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Which module it belongs to. so that for downloading completed task the break ups can be shown properly',
  `action` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'download action',
  `params` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `scheduled_by` int(11) NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expiry_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `expiry_time` (`expiry_time`),
  KEY `store_id` (`scheduled_by`)
) ENGINE=InnoDB AUTO_INCREMENT=11901 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : download