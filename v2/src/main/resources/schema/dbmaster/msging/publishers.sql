
-- start  Schema : publishers

CREATE TABLE `publishers` (
  `publisherId` int(11) NOT NULL,
  `categoryId` mediumint(9) NOT NULL AUTO_INCREMENT,
  `categoryCode` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  UNIQUE KEY `categoryId` (`categoryId`),
  UNIQUE KEY `publisherId` (`publisherId`,`categoryCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : publishers