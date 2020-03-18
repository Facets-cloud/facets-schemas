
-- start  Schema : facebook_adset_insights

CREATE TABLE `facebook_adset_insights` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `ads_account_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `adset_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `insights` text COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `cached_on` timestamp NULL DEFAULT NULL,
  `type` enum('FACEBOOK','GOOGLE','TWITTER') COLLATE utf8mb4_unicode_ci DEFAULT 'FACEBOOK',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `auto_update_time` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : facebook_adset_insights