
-- start  Schema : cashier_targets

CREATE TABLE `cashier_targets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `cashier_id` int(11) NOT NULL DEFAULT '-1',
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `target` int(11) NOT NULL,
  `target_achieved` tinyint(1) NOT NULL,
  `achieved_time` datetime NOT NULL,
  `cashier_code` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id1` (`org_id`,`cashier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : cashier_targets