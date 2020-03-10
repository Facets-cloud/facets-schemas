
-- start  Schema : m_static_variables_data

CREATE TABLE `m_static_variables_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `value` mediumtext NOT NULL,
  `datatype` enum('String','Number','StringList','NumberList') NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` int(11) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `IX_OrgId_IsActive_Key` (`org_id`,`is_active`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- end  Schema : m_static_variables_data