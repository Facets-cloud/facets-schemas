
-- start  Schema : ofMucAffiliation

CREATE TABLE `ofMucAffiliation` (
  `roomID` bigint(20) NOT NULL,
  `jid` text NOT NULL,
  `affiliation` tinyint(4) NOT NULL,
  PRIMARY KEY (`roomID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofMucAffiliation