
-- start  Schema : group_templates

CREATE TABLE `group_templates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL COMMENT 'refers to id in groups',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `ref_id_index` (`ref_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : group_templates