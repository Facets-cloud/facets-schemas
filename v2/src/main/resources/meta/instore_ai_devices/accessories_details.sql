
-- start  Schema : accessories_details

CREATE TABLE `accessories_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` bigint(20) NOT NULL,
  `with_rf_switch` tinyint(1) DEFAULT NULL,
  `device_id_desc` enum('VM','HM','CE','AC','PE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `hardware_extension` enum('17 cm','5 inch','1-2 feet','2-4 feet','3-6 feet','4-8 feet','5-10 feet','none') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `camera` enum('PiCam_V1','PiCam_V2','FE_H','FE_I','FE_J','none') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `case_color` enum('black','white') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `case_size` enum('small','large') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `microphone` enum('mic','none') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `device_id` (`device_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : accessories_details