
-- start  Schema : ofGroupUser

CREATE TABLE `ofGroupUser` (
  `groupName` varchar(50) NOT NULL,
  `username` varchar(100) NOT NULL,
  `administrator` tinyint(4) NOT NULL,
  PRIMARY KEY (`groupName`,`username`,`administrator`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofGroupUser