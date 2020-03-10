
-- start  Schema : banding

CREATE TABLE `banding` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `dim_table_id` int(11) NOT NULL,
  `column_id` int(11) NOT NULL,
  `type` enum('VALUE','DATE') NOT NULL DEFAULT 'VALUE',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `display_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `column_type` enum('STANDARD','EXTENDED_FIELD') NOT NULL DEFAULT 'STANDARD',
  `custom_table_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `band_org_unique` (`scope_id`,`name`),
  KEY `scope_dim_table` (`scope_id`,`dim_table_id`,`is_active`)
) ENGINE=InnoDB AUTO_INCREMENT=756 DEFAULT CHARSET=latin1;


-- end  Schema : banding