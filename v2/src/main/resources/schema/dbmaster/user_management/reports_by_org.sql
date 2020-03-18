
-- start  Schema : reports_by_org

CREATE TABLE `reports_by_org` (
  `org_id` int(11) NOT NULL,
  `report_id` int(11) NOT NULL,
  `print_enabled` int(1) DEFAULT '0',
  `export_enabled` int(1) DEFAULT '0',
  PRIMARY KEY (`org_id`,`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : reports_by_org