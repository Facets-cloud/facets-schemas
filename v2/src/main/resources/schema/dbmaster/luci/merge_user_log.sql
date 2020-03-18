
-- start  Schema : merge_user_log

CREATE TABLE `merge_user_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `from_user_id` int(11) NOT NULL,
  `to_user_id` int(11) NOT NULL,
  `merged_date` datetime NOT NULL,
  `till_id` int(11) NOT NULL,
  `request_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('SUCCESS','FAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `error_message` text COLLATE utf8mb4_unicode_ci,
  `notes` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_orgId_fromUserId_toUserId` (`org_id`,`from_user_id`,`to_user_id`),
  KEY `tdx_orgId_requestId` (`org_id`,`request_id`),
  KEY `org_id_merged_date_idx` (`org_id`,`merged_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : merge_user_log