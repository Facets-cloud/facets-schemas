
-- start  Schema : keyword_fact

CREATE TABLE `keyword_fact` (
  `org_id` int(11) DEFAULT NULL,
  `kpi_name` varchar(100) DEFAULT NULL,
  `old_kpi_value` varchar(100) DEFAULT NULL,
  `new_kpi_value` varchar(100) DEFAULT NULL,
  `exec_id` varchar(250) DEFAULT NULL,
  `oid` varchar(100) DEFAULT NULL,
  `open_id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : keyword_fact