
-- start  Schema : stores_info

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `stores_info` AS select `msu`.`org_id` AS `org_id`,`msu`.`id` AS `store_id`,`msu`.`client_version_num` AS `client_version_num`,`msu`.`compile_time` AS `compile_time`,`msu`.`svn_revision` AS `svn_revision`,`msu`.`last_updated_on` AS `last_updated`,NULL AS `region`,'' AS `city`,'' AS `state`,0 AS `size`,'' AS `type_of_area`,'' AS `tier_of_city`,'' AS `location`,cast(`msu`.`last_updated_on` as date) AS `established_on`,'' AS `address`,'' AS `address_2`,`ms`.`external_id` AS `external_store_id`,`ms`.`external_id_1` AS `external_store_id_1`,`ms`.`external_id_2` AS `external_store_id_2`,`msu`.`mac_addr` AS `mac_addr`,`msu`.`store_server_prefix` AS `store_server_prefix`,`msu`.`disable_mac_addr_check` AS `disable_mac_addr_check`,'' AS `time_zone_offset`,`ms`.`lat` AS `latitude`,`ms`.`long` AS `longitude` from (`masters`.`store_units` `msu` join `masters`.`stores` `ms`) where (`ms`.`id` = `msu`.`store_id`);


-- end  Schema : stores_info