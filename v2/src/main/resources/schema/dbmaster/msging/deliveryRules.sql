
-- start  Schema : deliveryRules

CREATE TABLE `deliveryRules` (
  `subscriberId` int(11) NOT NULL,
  `deliveryAgentId` int(11) NOT NULL,
  `isDelivered` tinyint(1) NOT NULL,
  PRIMARY KEY (`subscriberId`,`deliveryAgentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : deliveryRules