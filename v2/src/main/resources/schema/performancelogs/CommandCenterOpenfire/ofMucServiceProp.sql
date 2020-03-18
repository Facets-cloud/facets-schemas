
-- start  Schema : ofMucServiceProp

CREATE TABLE `ofMucServiceProp` (
  `serviceID` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`serviceID`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofMucServiceProp