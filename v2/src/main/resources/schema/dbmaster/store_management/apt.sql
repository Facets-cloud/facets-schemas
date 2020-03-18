
-- start  Schema : apt

CREATE TABLE `apt` (
  `id` longtext COLLATE utf8mb4_unicode_ci,
  `action_id` longtext COLLATE utf8mb4_unicode_ci,
  `permission_id` longtext COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : apt