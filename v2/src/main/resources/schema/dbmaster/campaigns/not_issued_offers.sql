
-- start  Schema : not_issued_offers

CREATE TABLE `not_issued_offers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `vch_series_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `bill_number` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rule_map` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : not_issued_offers