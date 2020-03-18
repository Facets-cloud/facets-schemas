
-- start  Schema : partner_program_slab_history

CREATE TABLE `partner_program_slab_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `loyalty_program_id` int(11) NOT NULL COMMENT 'Loyalty program to which the partner program is linked',
  `partner_program_id` int(11) NOT NULL COMMENT 'Partner program to which the customer is linked',
  `customer_id` bigint(20) NOT NULL,
  `last_slab_action` enum('UPGRADE','RENEWAL','ENROLLMENT') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Action due to which change in customer slab or slab expiry date occured',
  `slab_id` int(11) NOT NULL COMMENT 'Partner program slab in which the customer was present when this record was created',
  `slab_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name of the partner program slab in which the customer is present at this time',
  `slab_expiry_date` datetime NOT NULL COMMENT 'Expiry date of the partner program slab in which the customer is present at this time',
  `till_id` int(11) DEFAULT NULL COMMENT 'Till in context',
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `partner_program_id` (`org_id`,`loyalty_program_id`,`partner_program_id`,`customer_id`),
  KEY `created_on_idx` (`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : partner_program_slab_history