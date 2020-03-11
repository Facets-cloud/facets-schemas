
-- start  Schema : vs_kpis_sqls

CREATE TABLE `vs_kpis_sqls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `file_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'name of the sql file uploaded',
  `file_handler` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'file pointer received from file service',
  `download_link` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'downloadable link for the file',
  `priority` int(11) NOT NULL DEFAULT '100',
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_on` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL DEFAULT '-1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `file_handler` (`file_handler`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : vs_kpis_sqls