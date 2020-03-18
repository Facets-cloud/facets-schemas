
-- start  Schema : ofRRDs

CREATE TABLE `ofRRDs` (
  `id` varchar(100) NOT NULL,
  `updatedDate` bigint(20) NOT NULL,
  `bytes` mediumblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : ofRRDs