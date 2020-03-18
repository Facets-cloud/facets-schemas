
-- start  Schema : instructions_verification_log

CREATE TABLE `instructions_verification_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `last_verified_dump_id` int(11) NOT NULL,
  `last_verified_ep_dump_id` int(11) NOT NULL,
  `instructions_mismatch_summamry` mediumtext COLLATE utf8mb4_unicode_ci,
  `verification_status` enum('IN_PROGRESS','SUCCESS','FAIL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `verified_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : instructions_verification_log