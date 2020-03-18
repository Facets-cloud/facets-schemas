
-- start  Schema : countries

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `countries` AS select `masters`.`countries`.`id` AS `id`,`masters`.`countries`.`name` AS `name`,`masters`.`countries`.`capital` AS `capital`,`masters`.`countries`.`code` AS `code`,`masters`.`countries`.`short_name` AS `short_name`,`masters`.`countries`.`numeric_code` AS `numeric_code`,`masters`.`countries`.`iso_name` AS `iso_name`,`masters`.`countries`.`mobile_country_code` AS `mobile_country_code`,`masters`.`countries`.`mobile_regex` AS `mobile_regex`,`masters`.`countries`.`mobile_length_csv` AS `mobile_length_csv`,`masters`.`countries`.`last_updated_by` AS `last_updated_by`,`masters`.`countries`.`last_updated` AS `last_updated` from `masters`.`countries`;


-- end  Schema : countries