
-- start  Schema : ofGroupProp

CREATE TABLE `ofGroupProp` (
  `groupName` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`groupName`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofGroupProp