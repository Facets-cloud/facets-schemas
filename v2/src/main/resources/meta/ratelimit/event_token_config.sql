
-- start  Schema : event_token_config

CREATE TABLE `event_token_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resource` varchar(100) DEFAULT NULL,
  `method` varchar(100) NOT NULL,
  `http_method` varchar(11) DEFAULT NULL,
  `version` varchar(100) NOT NULL,
  `module` varchar(100) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `token_value` int(11) NOT NULL,
  `last_changed_date` datetime NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4;


-- end  Schema : event_token_config