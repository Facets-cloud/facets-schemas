
-- start  Schema : filter_execution

CREATE TABLE `filter_execution` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filter_id` int(11) NOT NULL,
  `execution_step_number` int(11) NOT NULL,
  `execution_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `execution_value` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `filter_id_step_type` (`filter_id`,`execution_step_number`,`execution_type`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : filter_execution