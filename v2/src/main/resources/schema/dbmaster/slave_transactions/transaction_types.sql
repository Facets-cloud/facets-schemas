
-- start  Schema : transaction_types

CREATE TABLE `transaction_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transaction_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : transaction_types