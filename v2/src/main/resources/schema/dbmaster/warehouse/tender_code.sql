
-- start  Schema : tender_code

CREATE TABLE `tender_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_tender_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `code` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `added_by` int(11) NOT NULL,
  `added_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `priority` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `program_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : tender_code