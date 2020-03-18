
-- start  Schema : api_error_codes

CREATE TABLE `api_error_codes` (
  `error_code` int(11) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `success` tinyint(1) DEFAULT '0',
  `auto_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : api_error_codes