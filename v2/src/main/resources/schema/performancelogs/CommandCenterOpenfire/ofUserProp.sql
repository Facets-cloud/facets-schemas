
-- start  Schema : ofUserProp

CREATE TABLE `ofUserProp` (
  `username` varchar(64) NOT NULL,
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`username`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofUserProp