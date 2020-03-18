
-- start  Schema : clusters

CREATE TABLE `clusters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `value_type` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `datatype_for_client` enum('String','Integer','Boolean','Double') COLLATE utf8mb4_unicode_ci NOT NULL,
  `sync_to_client` tinyint(1) NOT NULL,
  `params_json` longtext COLLATE utf8mb4_unicode_ci,
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id` (`org_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='store the different clusters for the org';


-- end  Schema : clusters