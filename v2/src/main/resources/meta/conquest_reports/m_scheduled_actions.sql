
-- start  Schema : m_scheduled_actions

CREATE TABLE `m_scheduled_actions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schedule_id` int(11) NOT NULL,
  `action` enum('EMAIL','SMS','FTP') DEFAULT NULL,
  `action_metadata` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=20097 DEFAULT CHARSET=utf8 COMMENT='Stores the action that need to be performed for a scheduled ';


-- end  Schema : m_scheduled_actions