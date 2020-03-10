
-- start  Schema : org_settings_new

CREATE TABLE `org_settings_new` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) DEFAULT NULL,
  `is_valid` int(11) DEFAULT NULL,
  `schema_name` varchar(64) DEFAULT NULL,
  `recent_bill_made` date DEFAULT NULL,
  `fresh_import_date` date NOT NULL,
  `previous_recent_bill_made` date DEFAULT NULL,
  `recent_redeemed_date` datetime DEFAULT NULL,
  `previous_recent_redeemed_date` datetime DEFAULT NULL,
  `custom_mapping_exists` tinyint(1) DEFAULT '0',
  KEY `id_index` (`id`),
  KEY `org_id_index` (`org_id`),
  KEY `is_valid_index` (`is_valid`),
  KEY `schema_name_index` (`schema_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


-- end  Schema : org_settings_new