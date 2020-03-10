
-- start  Schema : return_bill_lineitem

CREATE TABLE `return_bill_lineitem` (
  `org_id` int(11) DEFAULT NULL,
  `kpi_name` varchar(100) DEFAULT NULL,
  `old_kpi_value` varchar(100) DEFAULT NULL,
  `new_kpi_value` varchar(100) DEFAULT NULL,
  `exec_id` varchar(250) DEFAULT NULL,
  `bill_id` bigint(20) DEFAULT NULL,
  `line_item_id` bigint(20) DEFAULT NULL,
  `dim_membership_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : return_bill_lineitem