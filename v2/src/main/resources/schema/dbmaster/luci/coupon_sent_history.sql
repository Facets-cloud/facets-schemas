
-- start  Schema : coupon_sent_history

CREATE TABLE `coupon_sent_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `coupon_series_id` int(11) DEFAULT NULL,
  `coupon_issued_id` bigint(11) NOT NULL,
  `sent_date` datetime NOT NULL COMMENT 'system date on which coupon was sent',
  `till_id` bigint(20) NOT NULL COMMENT 'till id by which coupon was sent',
  `notes` tinytext COLLATE utf8mb4_unicode_ci COMMENT 'notes while sending the issued coupon',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`coupon_series_id`,`coupon_issued_id`),
  KEY `org_id_auto_update_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : coupon_sent_history