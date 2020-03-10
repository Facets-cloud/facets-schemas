
-- start  Schema : m_reports

CREATE TABLE `m_reports` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `r_name` varchar(256) NOT NULL,
  `r_desc` mediumtext,
  `rep_id` int(10) unsigned NOT NULL,
  `category_id` int(10) unsigned NOT NULL DEFAULT '1',
  `report_output_settings` mediumblob,
  `report_output_settings_json` mediumtext,
  `has_large_data` tinyint(1) NOT NULL,
  `is_external` tinyint(1) NOT NULL,
  `for_dashboard` tinyint(1) NOT NULL,
  `is_verified` tinyint(1) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `rep_id_index` (`rep_id`),
  KEY `category_id` (`category_id`),
  KEY `is_deleted` (`is_deleted`)
) ENGINE=MyISAM AUTO_INCREMENT=9846 DEFAULT CHARSET=utf8;


-- end  Schema : m_reports