
-- start  Schema : m_user_info

CREATE TABLE `m_user_info` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '-9999',
  `user_org_id` int(11) NOT NULL,
  `user_login` char(64) NOT NULL,
  `zoneConceptConfig` enum('ZONESANDCONCEPTS','ZONES','CONCEPTS') NOT NULL DEFAULT 'ZONES',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_login` (`user_login`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : m_user_info