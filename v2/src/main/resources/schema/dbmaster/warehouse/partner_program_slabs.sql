
-- start  Schema : partner_program_slabs

CREATE TABLE `partner_program_slabs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `loyalty_program_id` int(11) NOT NULL COMMENT 'Loyalty program to which the partner program is linked',
  `partner_program_id` int(11) NOT NULL COMMENT 'Partner program to which the slab belongs',
  `serial_number` smallint(6) NOT NULL COMMENT 'Serial number of the slab',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name of the slab',
  `description` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'Description of the slab',
  `created_on` datetime NOT NULL COMMENT 'Date on which the slab is created',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `partner_program_id` (`org_id`,`loyalty_program_id`,`partner_program_id`,`serial_number`),
  UNIQUE KEY `partner_program_id_name` (`org_id`,`loyalty_program_id`,`partner_program_id`,`name`),
  KEY `auto_time_idx` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : partner_program_slabs