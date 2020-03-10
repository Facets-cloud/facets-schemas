
-- start  Schema : m_scheduled_filters_segments

CREATE TABLE `m_scheduled_filters_segments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_id` int(11) NOT NULL,
  `json_value` mediumtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `schedule_id` (`schedule_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2558 DEFAULT CHARSET=utf8;


-- end  Schema : m_scheduled_filters_segments