
-- start  Schema : changelog

CREATE TABLE `changelog` (
  `id` int(11) NOT NULL,
  `file_name` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `query` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `undo_query` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `affected_table` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` enum('release','local') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'local',
  `dbservice` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `milestone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : changelog