
-- start  Schema : points

CREATE TABLE `points` (
  `org_id` int(11) DEFAULT NULL,
  `kpi_name` varchar(100) DEFAULT NULL,
  `old_kpi_value` varchar(100) DEFAULT NULL,
  `new_kpi_value` varchar(100) DEFAULT NULL,
  `exec_id` varchar(250) DEFAULT NULL,
  `dim_points_awarded_type_id` bigint(20) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `dim_points_event_type_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : points