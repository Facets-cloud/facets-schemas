
-- start  Schema : ofMessageArchive

CREATE TABLE `ofMessageArchive` (
  `messageID` bigint(20) DEFAULT NULL,
  `conversationID` bigint(20) NOT NULL,
  `fromJID` varchar(255) NOT NULL,
  `fromJIDResource` varchar(100) DEFAULT NULL,
  `toJID` varchar(255) NOT NULL,
  `toJIDResource` varchar(100) DEFAULT NULL,
  `sentDate` bigint(20) NOT NULL,
  `stanza` text,
  `body` text,
  KEY `ofMessageArchive_con_idx` (`conversationID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofMessageArchive