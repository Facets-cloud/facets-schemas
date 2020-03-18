
-- start  Schema : gc_base

CREATE TABLE `gc_base` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `card_no` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_id` int(11) NOT NULL,
  `added_on` datetime NOT NULL,
  `added_by` int(11) NOT NULL,
  `issued_to` int(11) NOT NULL DEFAULT '-1',
  `current_user` int(11) NOT NULL DEFAULT '-1',
  `issued_on` datetime NOT NULL,
  `issued_at` int(11) NOT NULL,
  `current_value` double NOT NULL DEFAULT '0',
  `lifetime_value` double NOT NULL DEFAULT '0',
  `encoded_card_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_user_uidx` (`org_id`,`encoded_card_no`),
  KEY `org_card_idx` (`org_id`,`encoded_card_no`),
  KEY `org_id` (`org_id`,`current_value`),
  KEY `org_id_2` (`org_id`,`lifetime_value`,`current_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : gc_base