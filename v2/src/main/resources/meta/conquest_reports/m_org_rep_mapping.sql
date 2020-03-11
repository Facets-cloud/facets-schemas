
-- start  Schema : m_org_rep_mapping

CREATE TABLE `m_org_rep_mapping` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `rep_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`,`rep_id`),
  KEY `rep_id_index` (`rep_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;


-- end  Schema : m_org_rep_mapping