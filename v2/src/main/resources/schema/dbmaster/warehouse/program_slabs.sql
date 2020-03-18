
-- start  Schema : program_slabs

CREATE TABLE `program_slabs` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'auto generated slab id',
  `org_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL COMMENT 'program to which the slab belongs to',
  `serial_number` int(11) NOT NULL COMMENT 'Indicates the slab numnber in the sequence',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'name of the slab',
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`org_id`,`program_id`,`serial_number`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : program_slabs