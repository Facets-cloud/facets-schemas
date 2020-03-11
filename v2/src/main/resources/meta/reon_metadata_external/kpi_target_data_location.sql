
-- start  Schema : kpi_target_data_location

CREATE TABLE `kpi_target_data_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `target_id` int(11) NOT NULL,
  `location` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uploaded_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `range_operand_dim` int(11) NOT NULL DEFAULT '0',
  `range_start_value` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `range_end_value` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `upload_status` enum('UPLOADED','VALIDATED','REJECTED','SUCCESS','INVALID','MERGED','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'UPLOADED',
  `org_id` int(11) NOT NULL DEFAULT '-1',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `period_unit_dim` int(11) NOT NULL DEFAULT '0',
  `added_on` timestamp NULL DEFAULT NULL,
  `error_file_location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : kpi_target_data_location