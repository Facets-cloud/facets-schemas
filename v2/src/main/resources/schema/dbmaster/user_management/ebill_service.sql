
-- start  Schema : ebill_service

CREATE TABLE `ebill_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `template_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `params` mediumblob NOT NULL,
  `status` enum('OPEN','PROCESSING','COMPLETED','ERROR') COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : ebill_service