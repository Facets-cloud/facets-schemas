
-- start  Schema : import_logs

CREATE TABLE `import_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `import_profile_type` enum('CUSTOMER','BILL','INVENTORY') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `db_details` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('SUCCESS','FAIL','REJECT','PARTIAL') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `records_uploaded` int(11) NOT NULL,
  `records_updated_to_db` int(11) NOT NULL,
  `imported_on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_logs