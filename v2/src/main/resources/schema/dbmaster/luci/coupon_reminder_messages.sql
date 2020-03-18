
-- start  Schema : coupon_reminder_messages

CREATE TABLE `coupon_reminder_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `coupon_series_id` int(11) NOT NULL,
  `coupon_reminder_id` int(11) NOT NULL,
  `type` enum('SMS','EMAIL','WECHAT','MOBILE_PUSH') COLLATE utf8mb4_unicode_ci NOT NULL,
  `message_json` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `coupon_reminder_id` (`org_id`,`coupon_series_id`,`coupon_reminder_id`),
  KEY `auto_update_time` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : coupon_reminder_messages