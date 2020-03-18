
-- start  Schema : gap_kpi_info

CREATE TABLE `gap_kpi_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL COMMENT 'ID of the organization for which gap is calculated',
  `program_id` int(11) NOT NULL COMMENT 'ID of the program for which gap is calculated',
  `kpi_type` enum('GAP_TO_UPGRADE','GAP_TO_RENEW') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Type of gap record',
  `kpi_version` int(11) NOT NULL COMMENT 'Version which is associated with record',
  `status` enum('IN_PROGRESS','VALID','INVALID') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `kpitype_version` (`org_id`,`program_id`,`kpi_type`,`kpi_version`),
  KEY `program_id` (`org_id`,`program_id`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : gap_kpi_info