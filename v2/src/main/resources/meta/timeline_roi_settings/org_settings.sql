
-- start  Schema : org_settings

CREATE TABLE `org_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) DEFAULT NULL,
  `is_valid` int(11) DEFAULT NULL,
  `is_sync_enabled` tinyint(4) DEFAULT '0',
  `schema_name` varchar(64) DEFAULT NULL,
  `recent_bill_made` datetime DEFAULT NULL,
  `recent_billdump_import_date` datetime DEFAULT NULL,
  `fresh_import_date` datetime DEFAULT NULL,
  `source_data_until` datetime DEFAULT NULL,
  `previous_recent_bill_made` datetime DEFAULT NULL,
  `recent_redeemed_date` datetime DEFAULT NULL,
  `previous_recent_redeemed_date` datetime DEFAULT NULL,
  `custom_mapping_exists` tinyint(1) DEFAULT '0',
  `schemaName` varchar(255) DEFAULT NULL,
  `is_being_updated` tinyint(1) DEFAULT '0',
  KEY `id_index` (`id`),
  KEY `org_id_index` (`org_id`),
  KEY `is_valid_index` (`is_valid`),
  KEY `schema_name_index` (`schema_name`)
) ENGINE=MyISAM AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;


-- end  Schema : org_settings