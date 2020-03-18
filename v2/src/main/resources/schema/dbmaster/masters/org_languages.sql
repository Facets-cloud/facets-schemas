
-- start  Schema : org_languages

CREATE TABLE `org_languages` (
  `id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `language_id` int(11) NOT NULL,
  UNIQUE KEY `org_id` (`org_id`,`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_languages