
-- start  Schema : bill_payment_details

CREATE TABLE `bill_payment_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `loyalty_log_id` int(11) NOT NULL COMMENT 'reference to bill entry in loyalty log',
  `type` int(11) NOT NULL COMMENT 'reference to the payment_types table',
  `submitted_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Adding this for redundancy right now, storing what types are sent by the clients',
  `amount` float NOT NULL DEFAULT '0',
  `notes` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : bill_payment_details