
-- start  Schema : contact_info

CREATE TABLE `contact_info` (
  `org_id` int(11) DEFAULT NULL,
  `kpi_name` varchar(100) DEFAULT NULL,
  `old_kpi_value` varchar(100) DEFAULT NULL,
  `new_kpi_value` varchar(100) DEFAULT NULL,
  `exec_id` varchar(250) DEFAULT NULL,
  `dim_communication_type_id` bigint(20) DEFAULT NULL,
  `msg_id` bigint(20) DEFAULT NULL,
  `dim_event_user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : contact_info