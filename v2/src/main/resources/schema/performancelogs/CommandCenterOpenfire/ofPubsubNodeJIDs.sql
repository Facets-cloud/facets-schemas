
-- start  Schema : ofPubsubNodeJIDs

CREATE TABLE `ofPubsubNodeJIDs` (
  `serviceID` varchar(100) NOT NULL,
  `nodeID` varchar(100) NOT NULL,
  `jid` varchar(255) NOT NULL,
  `associationType` varchar(20) NOT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofPubsubNodeJIDs