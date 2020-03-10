
-- start  Schema : entities

CREATE TABLE `entities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `type` enum('TILL','STORE','ZONE','CONCEPT','STR_SERVER','ADMIN_USER','ASSOCIATE') CHARACTER SET utf8 NOT NULL,
  `guid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Unique guid for the entity',
  `added_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `added_by` int(11) NOT NULL COMMENT 'Admin user id',
  `cluster` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Cluster to which this entity belongs',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_type_idx` (`org_id`,`type`,`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200202626 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : entities