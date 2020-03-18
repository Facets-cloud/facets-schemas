
-- start  Schema : ofVCard

CREATE TABLE `ofVCard` (
  `username` varchar(64) NOT NULL,
  `vcard` mediumtext NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofVCard