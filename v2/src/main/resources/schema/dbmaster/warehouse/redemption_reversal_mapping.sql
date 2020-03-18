
-- start  Schema : redemption_reversal_mapping

CREATE TABLE `redemption_reversal_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `points_reversed` decimal(15,3) NOT NULL,
  `reversal_id` int(11) NOT NULL COMMENT 'unique redemption id for points redemption reversal',
  `redemption_id` int(11) NOT NULL COMMENT 'reference to points redemption summary which is reversed',
  `created_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'time of redemption reversal',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `reversal_index` (`org_id`,`redemption_id`,`reversal_id`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : redemption_reversal_mapping