
-- start  Schema : hierarchical_dim_level_updates

CREATE TABLE `hierarchical_dim_level_updates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request_id` varchar(100) NOT NULL,
  `dim_table_id` int(11) NOT NULL,
  `dim_table_name` varchar(100) NOT NULL,
  `region` varchar(20) NOT NULL,
  `levels` text NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `status` enum('PENDING','PROCESSED','ERROR','CANCELLED','RESP_UPLOADED_TO_S3','RESP_UPLOADED_FROM_S3','REQ_UPLOADED_TO_S3','REQ_UPLOADED_FROM_S3') NOT NULL,
  `error` text,
  `scope_id` int(11) NOT NULL,
  `added_on` timestamp NULL DEFAULT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `request_key` (`request_id`,`region`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=481 DEFAULT CHARSET=latin1;


-- end  Schema : hierarchical_dim_level_updates