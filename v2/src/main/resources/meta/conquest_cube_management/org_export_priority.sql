
-- start  Schema : org_export_priority

CREATE TABLE `org_export_priority` (
  `org_id` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  UNIQUE KEY `org_id` (`org_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


-- end  Schema : org_export_priority