
-- start  Schema : mobile_operator_mapping

CREATE TABLE `mobile_operator_mapping` (
  `code` int(11) NOT NULL COMMENT 'first four digits of the mobile number',
  `operator_circle` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operator` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : mobile_operator_mapping