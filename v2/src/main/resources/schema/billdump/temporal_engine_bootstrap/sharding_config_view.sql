
-- start  Schema : sharding_config_view

CREATE ALGORITHM=UNDEFINED DEFINER=`dbrundeckadmin`@`%` SQL SECURITY DEFINER VIEW `sharding_config_view` AS select `sharding_config`.`id` AS `id`,`sharding_config`.`org_id` AS `org_id`,`sharding_config`.`org_config_id` AS `org_config_id`,`sharding_config`.`org_config_name` AS `org_config_name`,`sharding_config`.`campaign_id` AS `campaign_id`,`sharding_config`.`status` AS `status`,`sharding_config`.`database_name` AS `database_name`,`sharding_config`.`shard_id` AS `shard_id`,`sharding_config`.`auto_update_time` AS `auto_update_time` from `sharding_config` where (`sharding_config`.`status` = 'ACTIVE');


-- end  Schema : sharding_config_view