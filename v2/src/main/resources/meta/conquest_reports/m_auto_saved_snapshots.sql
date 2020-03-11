
-- start  Schema : m_auto_saved_snapshots

CREATE TABLE `m_auto_saved_snapshots` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `report_id` int(10) NOT NULL,
  `job_id` varchar(40) DEFAULT NULL,
  `fs_handle` varchar(40) DEFAULT NULL,
  `saved_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `IX_jobId` (`job_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : m_auto_saved_snapshots