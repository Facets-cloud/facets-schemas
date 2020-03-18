
-- start  Schema : user_family_mapping

CREATE TABLE `user_family_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `family_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `added_by` int(11) NOT NULL,
  `added_on` datetime NOT NULL,
  `is_member` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'IF you want to kick someone out from family',
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_family_mapping