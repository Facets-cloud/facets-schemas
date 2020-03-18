
-- start  Schema : selected_fields_for_reports

CREATE TABLE `selected_fields_for_reports` (
  `report_id` int(11) NOT NULL DEFAULT '0',
  `field_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : selected_fields_for_reports