
-- start  Schema : points_awarded_customer_promotions

CREATE TABLE `points_awarded_customer_promotions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `program_id` int(11) NOT NULL COMMENT 'ID of the program under which the points are awarded',
  `org_id` int(11) NOT NULL COMMENT 'The organization which awarded the points',
  `customer_id` int(11) NOT NULL,
  `original_customer_id` int(11) DEFAULT NULL,
  `promotion_id` int(11) NOT NULL COMMENT 'Bill under which the line item belongs to',
  `point_awarded_id` bigint(20) NOT NULL COMMENT 'Reference to points awarded table to which this entry is contributing',
  `point_category_id` int(11) NOT NULL COMMENT 'ID of the category under which the points are awarded',
  `status` enum('AVAILABLE','REDEEMED','EXPIRED','CONSUMED','RETURNED') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Available: Points can still be redeemed / expired. Redeemed: Fully consumed due to redemptions. Expired: All the points were expired. Consumed: Part of the points were redeemed, part expired. Cancelled: Points will be set to 0. Points should not be redeem',
  `allocation_strategy_id` int(11) NOT NULL COMMENT 'Allocation strategy used for awarding',
  `points_value` decimal(15,3) NOT NULL,
  `redeemed_value` decimal(15,3) NOT NULL,
  `expired_value` decimal(15,3) NOT NULL,
  `returned_value` decimal(15,3) NOT NULL,
  `original_points` decimal(15,3) DEFAULT '0.000',
  `awarded_date` datetime NOT NULL COMMENT 'Date when the point was awarded',
  `till_id` int(11) NOT NULL,
  `expiry_strategy_id` int(11) NOT NULL COMMENT 'Expriry strategy to be applied. (Needed to decided whether to perform a cascade on updating a expiry strategy)',
  `expiry_date` datetime NOT NULL COMMENT 'Date when the point will be expired. Decided when the point is created based on the current expiry strategy',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `program_id` (`org_id`,`program_id`,`customer_id`,`point_category_id`,`promotion_id`,`awarded_date`) USING BTREE,
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `expiry_date` (`program_id`,`point_category_id`,`status`,`expiry_date`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE,
  KEY `promo_details_index` (`org_id`,`program_id`,`promotion_id`,`awarded_date`),
  KEY `awarded_date` (`org_id`,`program_id`,`point_category_id`,`awarded_date`,`status`),
  KEY `idx_org_prog_origin_cust_ptct` (`org_id`,`program_id`,`original_customer_id`,`point_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_awarded_customer_promotions