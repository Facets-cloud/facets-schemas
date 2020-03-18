
-- start  Schema : partner_programs

CREATE TABLE `partner_programs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `loyalty_program_id` int(11) NOT NULL COMMENT 'Loyalty program to which the partner program is linked',
  `partner_program_identifier` varchar(127) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Extenal identifier of the partner program',
  `name` varchar(31) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name of the partner program',
  `description` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'Description of the partner program',
  `is_active` tinyint(1) NOT NULL COMMENT 'Checks if the partner program is active or not',
  `is_tier_based` tinyint(1) NOT NULL COMMENT 'Checks if the partner program has tiers',
  `points_exchange_ratio` decimal(15,3) NOT NULL COMMENT 'Loyalty program to partner program points conversion ratio',
  `created_on` datetime NOT NULL COMMENT 'Date of creation of partner program',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `partner_program_idx` (`org_id`,`loyalty_program_id`,`partner_program_identifier`),
  KEY `auto_update_time_idx` (`auto_update_time`),
  KEY `org_auto_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : partner_programs