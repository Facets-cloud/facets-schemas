
-- start  Schema : tmp_migration_metadata

CREATE TABLE `tmp_migration_metadata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_id` int(11) NOT NULL,
  `notification_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


-- end  Schema : tmp_migration_metadata