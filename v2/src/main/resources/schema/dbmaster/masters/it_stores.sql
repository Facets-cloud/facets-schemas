
-- start  Schema : it_stores

CREATE TABLE `it_stores` (
  `id` int(11) DEFAULT NULL,
  `store_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pincode` int(11) DEFAULT NULL,
  `lat` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `long` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : it_stores