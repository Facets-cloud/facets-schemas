
-- start  Schema : zones_hierarchy

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `zones_hierarchy` AS select `oe`.`org_id` AS `org_id`,`oer`.`child_entity_id` AS `zone_id`,`oer`.`parent_entity_id` AS `parent_id`,`oe`.`code` AS `zone_code`,`oe`.`description` AS `description`,'' AS `reporting_email`,'' AS `reporting_mobile` from (`masters`.`org_entities` `oe` join `masters`.`org_entity_relations` `oer`) where ((`oe`.`org_id` = `oer`.`org_id`) and (`oe`.`type` = 'ZONE') and (`oe`.`id` = `oer`.`child_entity_id`));


-- end  Schema : zones_hierarchy