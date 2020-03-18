
-- start  Schema : feedback

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL,
  `user_id` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `session_id` int(11) NOT NULL,
  `feedback` mediumtext COLLATE utf8mb4_unicode_ci,
  `nsadmin_id` bigint(20) NOT NULL DEFAULT '-1',
  `created_time` datetime DEFAULT NULL,
  `last_updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`,`user_id`,`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : feedback