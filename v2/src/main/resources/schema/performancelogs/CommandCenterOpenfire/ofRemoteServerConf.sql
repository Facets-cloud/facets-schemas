
-- start  Schema : ofRemoteServerConf

CREATE TABLE `ofRemoteServerConf` (
  `xmppDomain` varchar(255) NOT NULL,
  `remotePort` int(11) DEFAULT NULL,
  `permission` varchar(10) NOT NULL,
  PRIMARY KEY (`xmppDomain`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofRemoteServerConf