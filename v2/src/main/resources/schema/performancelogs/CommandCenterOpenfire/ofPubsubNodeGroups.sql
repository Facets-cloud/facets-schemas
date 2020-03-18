
-- start  Schema : ofPubsubNodeGroups

CREATE TABLE `ofPubsubNodeGroups` (
  `serviceID` varchar(100) NOT NULL,
  `nodeID` varchar(100) NOT NULL,
  `rosterGroup` varchar(100) NOT NULL,
  KEY `ofPubsubNodeGroups_idx` (`serviceID`,`nodeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofPubsubNodeGroups