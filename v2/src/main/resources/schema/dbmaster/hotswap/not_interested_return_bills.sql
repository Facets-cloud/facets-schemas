
-- start  Schema : not_interested_return_bills

CREATE TABLE `not_interested_return_bills` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `bill_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amount` double NOT NULL,
  `entered_by` int(11) NOT NULL,
  `returned_on` datetime NOT NULL,
  `loyalty_not_interested_bill_id` bigint(20) DEFAULT NULL COMMENT 'returns',
  `parent_loyalty_not_interested_bill_id` bigint(20) DEFAULT NULL COMMENT 'for mixed transactions - during which return has happened\n',
  `previous_till_id` int(11) NOT NULL,
  `previous_bill_date` datetime NOT NULL,
  `type` enum('FULL','LINE_ITEM','AMOUNT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `outlier_status` enum('NORMAL','INTERNAL','FRAUD','OUTLIER','TEST','FAILED','OTHER','RETRO','DELETED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parent_bill_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`bill_number`),
  KEY `returned_on` (`org_id`,`returned_on`),
  KEY `org_time_idx` (`auto_update_time`),
  KEY `org_parent_id_idx` (`org_id`,`parent_loyalty_not_interested_bill_id`),
  KEY `org_id_ni_bill_id` (`org_id`,`loyalty_not_interested_bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : not_interested_return_bills