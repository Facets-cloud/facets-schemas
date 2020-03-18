
-- start  Schema : upload_csv

CREATE TABLE `upload_csv` (
  `request_id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `key_type` enum('USER_ID','EXTERNAL_ID','MOBILE','EMAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `details` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_handler` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `session_id` text COLLATE utf8mb4_unicode_ci,
  `summary` mediumtext COLLATE utf8mb4_unicode_ci,
  `invalid_segments` mediumtext COLLATE utf8mb4_unicode_ci,
  `header` tinyint(1) NOT NULL,
  `error` mediumtext COLLATE utf8mb4_unicode_ci,
  `uploaded_by` int(11) NOT NULL,
  `uploaded_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : upload_csv