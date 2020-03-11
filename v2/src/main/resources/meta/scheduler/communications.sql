
-- start  Schema : communications

CREATE TABLE `communications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('EMAIL','SMS') COLLATE utf8mb4_unicode_ci NOT NULL,
  `subject` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_id` int(11) DEFAULT NULL,
  `admin_user_id` int(11) NOT NULL,
  `mobile` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `org_id` int(11) NOT NULL,
  `notification_id` int(11) NOT NULL,
  `status` enum('OPEN','RUNNING','EXECUTED','BOUNCED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPEN',
  `nsadmin_id` int(11) NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `status_id_idx` (`status`,`id`),
  KEY `org_id` (`org_id`,`notification_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : communications