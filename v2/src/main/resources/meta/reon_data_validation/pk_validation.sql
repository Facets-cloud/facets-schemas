
-- start  Schema : pk_validation

CREATE TABLE `pk_validation` (
  `db_name` varchar(50) DEFAULT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  `status` text,
  `mismatch` text,
  `pk_col` text NOT NULL,
  `exec_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- end  Schema : pk_validation