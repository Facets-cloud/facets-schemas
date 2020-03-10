
-- start  Schema : milestone_activity_fact

CREATE TABLE `milestone_activity_fact` (
  `org_id` int(11) DEFAULT NULL,
  `kpi_name` varchar(100) DEFAULT NULL,
  `old_kpi_value` varchar(100) DEFAULT NULL,
  `new_kpi_value` varchar(100) DEFAULT NULL,
  `exec_id` varchar(250) DEFAULT NULL,
  `history_id` bigint(20) DEFAULT NULL,
  `dim_org_config_id` bigint(20) DEFAULT NULL,
  `dim_communication_type_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : milestone_activity_fact