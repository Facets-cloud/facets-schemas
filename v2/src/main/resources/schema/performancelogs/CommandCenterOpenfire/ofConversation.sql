
-- start  Schema : ofConversation

CREATE TABLE `ofConversation` (
  `conversationID` bigint(20) NOT NULL,
  `room` varchar(255) DEFAULT NULL,
  `isExternal` tinyint(4) NOT NULL,
  `startDate` bigint(20) NOT NULL,
  `lastActivity` bigint(20) NOT NULL,
  `messageCount` int(11) NOT NULL,
  PRIMARY KEY (`conversationID`),
  KEY `ofConversation_ext_idx` (`isExternal`),
  KEY `ofConversation_start_idx` (`startDate`),
  KEY `ofConversation_last_idx` (`lastActivity`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofConversation