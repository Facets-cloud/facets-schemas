
-- start  Schema : goodwill_points_log

CREATE TABLE `goodwill_points_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `program_id` int(11) NOT NULL,
  `point_category_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `points_value` decimal(15,3) NOT NULL,
  `notes` longtext COLLATE utf8mb4_unicode_ci,
  `points_awarded_customer_promotions_id` int(11) DEFAULT NULL COMMENT ' Table points_awarded_customer_promotions id under which goodwilll points awarded',
  `goodwill_reference_id` int(11) NOT NULL,
  `till_id` int(11) NOT NULL,
  `request_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint(1) NOT NULL,
  `request_time` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : goodwill_points_log