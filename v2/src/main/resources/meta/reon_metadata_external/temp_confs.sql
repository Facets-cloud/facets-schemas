
-- start  Schema : temp_confs

CREATE TABLE `temp_confs` (
  `org_id` bigint(20) NOT NULL DEFAULT '0',
  `value` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : temp_confs