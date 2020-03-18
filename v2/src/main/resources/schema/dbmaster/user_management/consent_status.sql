
-- start  Schema : consent_status

CREATE TABLE `consent_status` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL DEFAULT '1',
  `mobile` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `consent_status` tinyint(1) DEFAULT NULL,
  `is_consent_required` tinyint(1) DEFAULT NULL,
  `added_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`org_id`,`mobile`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : consent_status