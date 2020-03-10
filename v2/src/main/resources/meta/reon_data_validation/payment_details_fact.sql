
-- start  Schema : payment_details_fact

CREATE TABLE `payment_details_fact` (
  `org_id` int(11) DEFAULT NULL,
  `kpi_name` varchar(100) DEFAULT NULL,
  `old_kpi_value` varchar(100) DEFAULT NULL,
  `new_kpi_value` varchar(100) DEFAULT NULL,
  `payment_mode_details_id` bigint(20) DEFAULT NULL,
  `bill_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : payment_details_fact