
-- start  Schema : m_scheduled_run_input_values

CREATE TABLE `m_scheduled_run_input_values` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `schedule_id` int(10) NOT NULL,
  `stage_seq_number` int(10) NOT NULL,
  `json_input_value` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=336262 DEFAULT CHARSET=utf8 COMMENT='Stores the sample input values which will be used by the sch';


-- end  Schema : m_scheduled_run_input_values