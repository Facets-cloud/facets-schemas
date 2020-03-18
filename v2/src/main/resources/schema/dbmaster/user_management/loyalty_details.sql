
-- start  Schema : loyalty_details

CREATE TABLE `loyalty_details` (
  `loyalty_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `registered_at` int(11) NOT NULL,
  `first_bill_store` int(11) NOT NULL,
  `last_bill_store` int(11) NOT NULL,
  `count_last_bill_store` int(11) NOT NULL,
  `majority_store` int(11) NOT NULL,
  `count_of_majority_store` int(11) NOT NULL,
  `total_bills` int(11) NOT NULL,
  `json_fields` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`loyalty_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loyalty_details