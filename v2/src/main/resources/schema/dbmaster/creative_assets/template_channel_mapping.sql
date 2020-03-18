
-- start  Schema : template_channel_mapping

CREATE TABLE `template_channel_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` int(11) NOT NULL,
  `template_type_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `ref_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `last_updated_by` int(11) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `template_id` (`template_id`,`template_type_id`,`org_id`,`ref_id`,`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : template_channel_mapping