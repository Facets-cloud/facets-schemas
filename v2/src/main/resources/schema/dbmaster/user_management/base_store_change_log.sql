
-- start  Schema : base_store_change_log

CREATE TABLE `base_store_change_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loyalty_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `registered_at` int(11) NOT NULL,
  `prev_base_store` int(11) NOT NULL,
  `curr_base_store` int(11) NOT NULL,
  `rule_applied` int(11) NOT NULL,
  `date` date NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `loyalty_id` (`loyalty_id`,`org_id`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : base_store_change_log