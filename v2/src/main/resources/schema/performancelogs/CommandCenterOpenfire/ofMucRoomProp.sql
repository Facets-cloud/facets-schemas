
-- start  Schema : ofMucRoomProp

CREATE TABLE `ofMucRoomProp` (
  `roomID` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `propValue` text NOT NULL,
  PRIMARY KEY (`roomID`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofMucRoomProp