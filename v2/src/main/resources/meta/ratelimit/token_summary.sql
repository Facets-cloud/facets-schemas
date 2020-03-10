
-- start  Schema : token_summary

CREATE TABLE `token_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `allotted` int(11) NOT NULL,
  `consumed` int(11) NOT NULL,
  `expired` int(11) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- end  Schema : token_summary