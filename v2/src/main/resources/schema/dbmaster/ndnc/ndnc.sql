
-- start  Schema : ndnc

CREATE TABLE `ndnc` (
  `mobile` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `ndnc` tinyint(1) NOT NULL,
  PRIMARY KEY (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : ndnc