
-- start  Schema : merchant_org_source

CREATE TABLE `merchant_org_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(50) NOT NULL,
  `org_id` int(11) NOT NULL,
  `account_id` varchar(50) DEFAULT NULL,
  `uuid` varchar(50) NOT NULL,
  `account_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


-- end  Schema : merchant_org_source