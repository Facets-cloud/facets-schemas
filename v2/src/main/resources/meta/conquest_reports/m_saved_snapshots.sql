
-- start  Schema : m_saved_snapshots

CREATE TABLE `m_saved_snapshots` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `report_id` int(10) NOT NULL,
  `fs_handle` varchar(50) NOT NULL,
  `tag_name` varchar(256) NOT NULL,
  `org_id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `serialized_ui_filters` mediumtext,
  `saved_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `IX_OrgId_UserId_IsActive` (`org_id`,`user_id`,`is_active`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;


-- end  Schema : m_saved_snapshots