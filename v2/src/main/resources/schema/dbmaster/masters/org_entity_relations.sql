
-- start  Schema : org_entity_relations

CREATE TABLE `org_entity_relations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `parent_entity_id` int(11) NOT NULL,
  `parent_entity_type` enum('ZONE','CONCEPT','STORE','STR_SERVER') COLLATE utf8mb4_unicode_ci NOT NULL,
  `child_entity_id` int(11) NOT NULL,
  `child_entity_type` enum('ZONE','CONCEPT','STORE','STR_SERVER','TILL','ASSOCIATE') CHARACTER SET utf8 NOT NULL,
  `code` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `parent_entity_id` (`parent_entity_id`,`child_entity_id`),
  UNIQUE KEY `parent_entity_type` (`parent_entity_type`,`child_entity_id`),
  KEY `code` (`code`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_entity_relations