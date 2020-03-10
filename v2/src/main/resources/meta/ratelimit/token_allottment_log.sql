
-- start  Schema : token_allottment_log

CREATE TABLE `token_allottment_log` (
  `id` int(11) NOT NULL DEFAULT '0',
  `org_id` int(11) NOT NULL,
  `allocated_date` datetime NOT NULL,
  `expiry_date` datetime NOT NULL,
  `value` int(11) NOT NULL,
  `created_on` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `last_modified_on` datetime NOT NULL,
  `last_modified_by` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- end  Schema : token_allottment_log