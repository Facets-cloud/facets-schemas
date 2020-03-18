
-- start  Schema : client_file_mappings

CREATE TABLE `client_file_mappings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `mapping_type` enum('data_providers_file_mapping','printer_template_file_mapping','integration_output_template_file_mapping','integration_post_output_file_mapping','client_log_file_mapping','lego_properties_file_mapping','client_log_config_file_mapping') COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `created_time` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`mapping_type`,`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='Mappings of the files stored for different purposes';


-- end  Schema : client_file_mappings