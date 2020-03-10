
-- start  Schema : cdm_failure_details

CREATE TABLE `cdm_failure_details` (
  `org_id` int(11) DEFAULT NULL,
  `table_name` varchar(50) DEFAULT NULL,
  `test_case_name` varchar(255) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `mismatch` text,
  `exec_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : cdm_failure_details