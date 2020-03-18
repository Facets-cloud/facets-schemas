
-- start  Schema : voucher_product_meta_data

CREATE TABLE `voucher_product_meta_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_type` (`product_type`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : voucher_product_meta_data