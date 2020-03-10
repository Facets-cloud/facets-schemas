
-- start  Schema : email_click_stats

CREATE TABLE `email_click_stats` (
  `org_id` int(11) DEFAULT NULL,
  `kpi_name` varchar(100) DEFAULT NULL,
  `old_kpi_value` varchar(100) DEFAULT NULL,
  `new_kpi_value` varchar(100) DEFAULT NULL,
  `exec_id` varchar(250) DEFAULT NULL,
  `id` bigint(20) DEFAULT NULL,
  `link_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : email_click_stats