
-- start  Schema : incentive_generic_meta_details

CREATE TABLE `incentive_generic_meta_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `generic_objective_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `help_text` text COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : incentive_generic_meta_details