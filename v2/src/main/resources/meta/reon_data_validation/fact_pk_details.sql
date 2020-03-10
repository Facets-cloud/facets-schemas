
-- start  Schema : fact_pk_details

CREATE TABLE `fact_pk_details` (
  `org_id` int(11) NOT NULL,
  `fact_table` varchar(50) DEFAULT NULL,
  `pk_col` text NOT NULL,
  `status` text,
  `mismatch` text,
  `failure_values` text NOT NULL,
  `exec_id` int(11) DEFAULT NULL,
  KEY `oidexec` (`org_id`,`exec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : fact_pk_details