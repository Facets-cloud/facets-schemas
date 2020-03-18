
-- start  Schema : customer_notes

CREATE TABLE `customer_notes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `assoc_id` int(20) NOT NULL DEFAULT '-1',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by` int(20) NOT NULL,
  `added_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : customer_notes