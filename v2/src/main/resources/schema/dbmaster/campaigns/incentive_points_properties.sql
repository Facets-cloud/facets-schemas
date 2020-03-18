
-- start  Schema : incentive_points_properties

CREATE TABLE `incentive_points_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `allocation_strategy_id` int(11) DEFAULT NULL,
  `expiry_strategy_id` int(11) DEFAULT NULL,
  `program_id` int(11) NOT NULL,
  `last_updated` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : incentive_points_properties