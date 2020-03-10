
-- start  Schema : ftp_sources_templates_audit

CREATE TABLE `ftp_sources_templates_audit` (
  `org_id` int(11) NOT NULL,
  `source_id` int(11) NOT NULL,
  `source_details` text COLLATE utf8mb4_unicode_ci COMMENT 'json of details about ftp source',
  `template_mapping` text COLLATE utf8mb4_unicode_ci COMMENT 'json of details about ftp template mapping',
  `cron_details` text COLLATE utf8mb4_unicode_ci COMMENT 'json of reminder details associated with ftp source ',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int(11) NOT NULL,
  KEY `org_key` (`org_id`,`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : ftp_sources_templates_audit