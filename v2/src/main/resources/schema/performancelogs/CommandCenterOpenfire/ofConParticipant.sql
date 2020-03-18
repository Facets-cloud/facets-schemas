
-- start  Schema : ofConParticipant

CREATE TABLE `ofConParticipant` (
  `conversationID` bigint(20) NOT NULL,
  `joinedDate` bigint(20) NOT NULL,
  `leftDate` bigint(20) DEFAULT NULL,
  `bareJID` varchar(200) NOT NULL,
  `jidResource` varchar(100) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  KEY `ofConParticipant_conv_idx` (`conversationID`,`bareJID`,`jidResource`,`joinedDate`),
  KEY `ofConParticipant_jid_idx` (`bareJID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofConParticipant