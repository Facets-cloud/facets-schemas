
-- start  Schema : org_last_insert

CREATE TABLE `org_last_insert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_id` int(11) DEFAULT NULL,
  `facttable` varchar(64) DEFAULT NULL,
  `Copied_Upto` int(11) DEFAULT NULL,
  `Copied_From` int(11) DEFAULT NULL,
  `IS_Active` tinyint(1) DEFAULT '1',
  UNIQUE KEY `fk_id` (`fk_id`,`facttable`),
  KEY `id_index` (`id`),
  KEY `fk_id_index` (`fk_id`),
  KEY `facttable_id` (`facttable`),
  KEY `Copied_Upto_Index` (`Copied_Upto`),
  KEY `FKC4810FA7ADE33771` (`fk_id`),
  KEY `FKC4810FA766EBDD2E` (`fk_id`)
) ENGINE=MyISAM AUTO_INCREMENT=59099 DEFAULT CHARSET=latin1;


-- end  Schema : org_last_insert