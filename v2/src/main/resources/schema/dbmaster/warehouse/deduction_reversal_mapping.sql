
-- start  Schema : deduction_reversal_mapping

CREATE TABLE `deduction_reversal_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `reverse_deduction_id` bigint(20) NOT NULL COMMENT 'deduction id for points deduction reversal',
  `deduction_id` bigint(20) NOT NULL COMMENT 'reference to points_deduction which is reversed',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `deduction_index` (`org_id`,`deduction_id`,`reverse_deduction_id`),
  KEY `reversal_index` (`org_id`,`reverse_deduction_id`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : deduction_reversal_mapping