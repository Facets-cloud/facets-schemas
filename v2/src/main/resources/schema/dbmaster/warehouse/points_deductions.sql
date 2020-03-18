
-- start  Schema : points_deductions

CREATE TABLE `points_deductions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `program_id` int(11) NOT NULL COMMENT 'ID of the program under which the points have been consumed',
  `org_id` int(11) NOT NULL COMMENT 'The organization under which the points were deducted',
  `customer_id` int(11) NOT NULL,
  `point_awarded_ref_type` enum('POINT_AWARDED','POINT_AWARDED_BILL_PROMOTION','POINT_AWARDED_LINEITEM','POINT_AWARDED_LINEITEM_PROMOTION','POINT_AWARDED_CUSTOMER_PROMOTION') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `point_awarded_ref_id` bigint(20) NOT NULL COMMENT 'Reference to the points awarded table',
  `deduction_type` enum('REDEEMED','EXPIRED','CANCELLED','MIGRATION','RETURN','REDEEMED_BY_TRANSFER','REDEMPTION_REVERSAL','EXPIRY_REVERTED','REDEMPTION_REVERTED','REDEEMED_BY_TRANSFER_REVERTED') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deduction_summary_id` int(11) NOT NULL DEFAULT '-1',
  `source_type_id` int(11) NOT NULL,
  `source_id` bigint(20) NOT NULL COMMENT 'ref id against which the redemption was made',
  `source_indentification_info` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Information about the source',
  `points_deducted` decimal(15,3) NOT NULL,
  `deduction_currency_value` decimal(15,3) NOT NULL DEFAULT '0.000',
  `deducted_on` datetime NOT NULL COMMENT 'Date when the point was redeemed/expired/cancelled',
  `till_id` int(11) NOT NULL,
  `awarded_till_id` int(11) DEFAULT '-1',
  `awarded_till_date` datetime DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE,
  KEY `program_id` (`org_id`,`program_id`,`customer_id`,`point_awarded_ref_type`,`point_awarded_ref_id`,`deduction_type`,`deducted_on`),
  KEY `deduction_summary_idx` (`org_id`,`deduction_summary_id`,`deduction_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_deductions