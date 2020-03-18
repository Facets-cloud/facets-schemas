
-- start  Schema : points_awarded_tender

CREATE TABLE `points_awarded_tender` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `program_id` int(11) NOT NULL,
  `point_category_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `bill_id` bigint(20) NOT NULL,
  `ref_type` enum('POINT_AWARDED','POINT_AWARDED_BILL_PROMOTION','POINT_AWARDED_LINEITEM','POINT_AWARDED_LINEITEM_PROMOTION','POINT_AWARDED_CUSTOMER_PROMOTION') COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_id` bigint(20) NOT NULL,
  `points` decimal(15,3) NOT NULL,
  `tender_code_id` int(11) NOT NULL,
  `awarded_date` datetime NOT NULL,
  `awarded_by` int(11) NOT NULL,
  `expiry_date` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE,
  KEY `auto_update_time` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : points_awarded_tender