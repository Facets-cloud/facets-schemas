
-- start  Schema : properties

CREATE TABLE `properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attribute` varchar(30) NOT NULL,
  `value` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `attribute` (`attribute`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;


-- end  Schema : properties