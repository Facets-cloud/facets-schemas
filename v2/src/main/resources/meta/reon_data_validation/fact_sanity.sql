
-- start  Schema : fact_sanity

CREATE TABLE `fact_sanity` (
  `org_id` int(11) NOT NULL,
  `table_name` text,
  `year` text,
  `status` text,
  `mismatch` text,
  `extra` text,
  `exec_id` int(11) DEFAULT NULL,
  KEY `org_id` (`org_id`,`exec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : fact_sanity