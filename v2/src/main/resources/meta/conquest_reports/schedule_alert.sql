
-- start  Schema : schedule_alert

CREATE TABLE `schedule_alert` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `alert_time` datetime NOT NULL,
  `alert_date` date NOT NULL,
  `is_valid` tinyint(1) DEFAULT '1',
  `is_sent` tinyint(4) DEFAULT '0',
  `email_id` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


-- end  Schema : schedule_alert