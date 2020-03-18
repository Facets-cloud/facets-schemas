
-- start  Schema : emf_dvs_verification_log

CREATE TABLE `emf_dvs_verification_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL DEFAULT '0',
  `last_verified_id` int(11) DEFAULT NULL,
  `verification_status` enum('IN_PROGRESS','SUCCESS','FAIL') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `exception` mediumtext COLLATE utf8mb4_unicode_ci,
  `verified_on` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : emf_dvs_verification_log