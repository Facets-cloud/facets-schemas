
-- start  Schema : custom_fields_for_reports

CREATE TABLE `custom_fields_for_reports` (
  `report_id` int(11) NOT NULL DEFAULT '0',
  `cf_id` int(11) NOT NULL DEFAULT '0',
  `field_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`report_id`,`cf_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : custom_fields_for_reports