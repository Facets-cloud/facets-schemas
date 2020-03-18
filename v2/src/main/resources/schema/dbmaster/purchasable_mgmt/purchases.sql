
-- start  Schema : purchases

CREATE TABLE `purchases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `purchasable_id` int(11) NOT NULL,
  `status` enum('ACTIVE','SUSPENDED','CLOSED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `valid_from` datetime NOT NULL,
  `valid_till` datetime NOT NULL,
  `invoice_id` int(11) DEFAULT NULL,
  `notes` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `qty` int(11) NOT NULL DEFAULT '0',
  `purchased_by` int(11) NOT NULL,
  `purchased_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Information about the packages, plans and top ups purchased ';


-- end  Schema : purchases