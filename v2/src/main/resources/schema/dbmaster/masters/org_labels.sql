
-- start  Schema : org_labels

CREATE TABLE `org_labels` (
  `Id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `Org_id` bigint(11) unsigned NOT NULL,
  `last_updated_by` int(11) unsigned NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Label name',
  `description` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Label description',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `orgIdlabelName` (`Org_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : org_labels