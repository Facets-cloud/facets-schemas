
-- start  Schema : store_perf_metrics

CREATE TABLE `store_perf_metrics` (
  `org_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `bills_per_day_weekday` float NOT NULL,
  `bills_per_day_weekend` float NOT NULL,
  `last_updated` datetime NOT NULL,
  PRIMARY KEY (`org_id`,`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Stores the metrics used to calculate store performance';


-- end  Schema : store_perf_metrics