
-- start  Schema : gc_transaction_log

CREATE TABLE `gc_transaction_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `card_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `type` enum('DEBIT','CREDIT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` double NOT NULL,
  `added_on` datetime NOT NULL,
  `store_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '-1',
  `prev_value` double NOT NULL,
  `bill_no` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `transaction_id` int(11) NOT NULL DEFAULT '-1',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `card_date_uniq_idx` (`card_id`,`type`,`amount`,`added_on`,`store_id`,`is_valid`),
  KEY `org_card_idx` (`org_id`,`card_id`),
  KEY `org_user_idx` (`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : gc_transaction_log