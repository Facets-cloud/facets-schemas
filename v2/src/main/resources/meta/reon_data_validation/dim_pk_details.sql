
-- start  Schema : dim_pk_details

CREATE TABLE `dim_pk_details` (
  `org_id` int(11) NOT NULL,
  `dim_table` varchar(50) DEFAULT NULL,
  `pk_col` text NOT NULL,
  `status` text,
  `mismatch` text,
  `failure_values` text NOT NULL,
  `exec_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : dim_pk_details