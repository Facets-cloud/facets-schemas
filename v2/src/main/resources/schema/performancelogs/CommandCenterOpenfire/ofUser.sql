
-- start  Schema : ofUser

CREATE TABLE `ofUser` (
  `username` varchar(64) NOT NULL,
  `storedKey` varchar(32) DEFAULT NULL,
  `serverKey` varchar(32) DEFAULT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `iterations` int(11) DEFAULT NULL,
  `plainPassword` varchar(32) DEFAULT NULL,
  `encryptedPassword` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `creationDate` char(15) NOT NULL,
  `modificationDate` char(15) NOT NULL,
  PRIMARY KEY (`username`),
  KEY `ofUser_cDate_idx` (`creationDate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofUser