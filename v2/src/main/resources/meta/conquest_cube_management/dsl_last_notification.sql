
-- start  Schema : dsl_last_notification

CREATE TABLE `dsl_last_notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `sync_type` enum('DSL_SYNC','ROI_COMPUTATION') NOT NULL,
  `end_time` datetime NOT NULL,
  `execution_time` bigint(20) NOT NULL,
  `was_incremental` tinyint(1) DEFAULT NULL,
  `was_successful` tinyint(1) DEFAULT NULL,
  `previous_end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_sync` (`org_id`,`sync_type`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


-- end  Schema : dsl_last_notification