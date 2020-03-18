
-- start  Schema : points_split_history

CREATE TABLE `points_split_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `loyalty_id` int(11) NOT NULL,
  `reference_id` int(11) NOT NULL,
  `loyalty_redemption_id` int(11) DEFAULT NULL,
  `debited_at` int(11) NOT NULL,
  `redeemed_at` int(11) NOT NULL,
  `points_debited` int(11) NOT NULL,
  `debited_date` datetime NOT NULL,
  `type` enum('bill','awarded','bill_expiry','awarded_point_expiry') COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`loyalty_id`),
  KEY `org_id_2` (`org_id`,`reference_id`),
  KEY `org_id_3` (`org_id`,`debited_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_split_history