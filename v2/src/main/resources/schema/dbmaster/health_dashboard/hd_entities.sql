
-- start  Schema : hd_entities

CREATE TABLE `hd_entities` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `entity_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `entity_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_timestamp_idx` (`id`,`added_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : hd_entities