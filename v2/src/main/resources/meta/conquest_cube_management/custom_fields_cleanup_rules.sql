
-- start  Schema : custom_fields_cleanup_rules

CREATE TABLE `custom_fields_cleanup_rules` (
  `cf_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `label` mediumtext NOT NULL,
  `cleanup_rule` varchar(30) NOT NULL,
  `cleaned_up_column` varchar(100) NOT NULL,
  `is_disabled` tinyint(1) NOT NULL,
  `source_last_updated` datetime DEFAULT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last Rule Updated Time',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto increment to faciliate sqoop',
  PRIMARY KEY (`cf_id`),
  UNIQUE KEY `id` (`id`),
  KEY `source_last_updated` (`source_last_updated`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;


-- end  Schema : custom_fields_cleanup_rules