
-- start  Schema : org_base_tiny_url

CREATE TABLE `org_base_tiny_url` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(20) NOT NULL,
  `base_tiny_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : org_base_tiny_url