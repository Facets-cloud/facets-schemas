
-- start  Schema : coupon_message_details

CREATE TABLE `coupon_message_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `transaction_id` bigint(20) NOT NULL,
  `nsadmin_id` bigint(20) NOT NULL,
  `sent_messages_log_id` bigint(20) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `coupon_id` int(11) NOT NULL,
  `campaign_id` int(11) NOT NULL,
  `added_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `auto_update_time` (`org_id`,`auto_update_time`),
  KEY `coupon_index` (`org_id`,`customer_id`,`sent_messages_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : coupon_message_details