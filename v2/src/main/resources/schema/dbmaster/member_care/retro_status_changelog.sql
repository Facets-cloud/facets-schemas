
-- start  Schema : retro_status_changelog

CREATE TABLE `retro_status_changelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key ',
  `retro_request_id` int(11) NOT NULL DEFAULT '-1' COMMENT 'Retro-request ID (Foreign Key)',
  `status` enum('PENDING','APPROVED','REJECTED','CUSTOM') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT 'Status of Retro Request',
  `reason` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Reason For The Status Selected',
  `comments` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Comments On The Status Selected',
  `updated_by` int(11) NOT NULL DEFAULT '-1' COMMENT 'ID Of The Status Updator',
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time When The Status Was Updated',
  PRIMARY KEY (`id`),
  KEY `retro_request_id` (`retro_request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='This table records of all the changes made to the status of a retro-request (1:M)';


-- end  Schema : retro_status_changelog