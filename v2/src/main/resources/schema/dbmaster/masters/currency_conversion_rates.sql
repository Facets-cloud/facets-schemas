
-- start  Schema : currency_conversion_rates

CREATE TABLE `currency_conversion_rates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `from_currency_id` int(11) NOT NULL,
  `to_currency_id` int(11) NOT NULL,
  `conversion_rate` float NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `valid_till` datetime NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `parent_id` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`),
  KEY `from_currency_id` (`from_currency_id`),
  KEY `to_currency_id` (`to_currency_id`),
  KEY `last_updated_by` (`last_updated_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : currency_conversion_rates