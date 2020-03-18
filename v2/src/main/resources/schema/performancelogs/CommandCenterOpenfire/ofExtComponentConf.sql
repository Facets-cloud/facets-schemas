
-- start  Schema : ofExtComponentConf

CREATE TABLE `ofExtComponentConf` (
  `subdomain` varchar(255) NOT NULL,
  `wildcard` tinyint(4) NOT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `permission` varchar(10) NOT NULL,
  PRIMARY KEY (`subdomain`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofExtComponentConf