
-- start  Schema : customer_satisfaction

CREATE TABLE `customer_satisfaction` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned NOT NULL,
  `survey_form_id` int(11) unsigned NOT NULL,
  `first_nps_score` int(11) unsigned NOT NULL,
  `recent_nps_score` int(11) unsigned NOT NULL,
  `mean_nps_score` int(11) unsigned NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : customer_satisfaction