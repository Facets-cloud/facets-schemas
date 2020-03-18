
-- start  Schema : reminder_messages_sent

CREATE TABLE `reminder_messages_sent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `reminder_message_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `message_id` bigint(20) NOT NULL,
  `sent_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `reminder_message_id` (`org_id`,`coupon_series_id`,`reminder_message_id`),
  KEY `customer_id` (`org_id`,`coupon_series_id`,`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : reminder_messages_sent