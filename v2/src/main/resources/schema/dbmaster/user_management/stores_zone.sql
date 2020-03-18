
-- start  Schema : stores_zone

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `stores_zone` AS select `till_rel`.`org_id` AS `org_id`,`till_rel`.`child_entity_id` AS `store_id`,`stores_rel`.`parent_entity_id` AS `zone_id` from (`masters`.`org_entity_relations` `stores_rel` join `masters`.`org_entity_relations` `till_rel`) where ((`stores_rel`.`parent_entity_type` = 'ZONE') and (`stores_rel`.`child_entity_type` = 'STORE') and (`stores_rel`.`child_entity_id` = `till_rel`.`parent_entity_id`) and (`till_rel`.`child_entity_type` = 'TILL'));


-- end  Schema : stores_zone