
-- start  Schema : assoc_activity

CREATE TABLE `assoc_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `assoc_id` int(11) NOT NULL,
  `type` enum('REG_TRANSACTION','REGISTRATION','CUSTOMER_UPDATE','RET_TRANSACTION','NI_TRANSACTION','NI_RETURN_TRANSACTION') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_id` int(11) NOT NULL,
  `till_id` int(11) NOT NULL,
  `added_on` datetime NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : assoc_activity