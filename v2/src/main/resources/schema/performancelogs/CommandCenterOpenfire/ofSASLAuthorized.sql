
-- start  Schema : ofSASLAuthorized

CREATE TABLE `ofSASLAuthorized` (
  `username` varchar(64) NOT NULL,
  `principal` text NOT NULL,
  PRIMARY KEY (`username`,`principal`(200))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofSASLAuthorized