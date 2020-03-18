
-- start  Schema : purchased_features

CREATE TABLE `purchased_features` (
  `org_id` int(11) NOT NULL,
  `purchase_id` int(11) NOT NULL,
  `feature_id` int(11) NOT NULL,
  `feature_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `populated_on` datetime NOT NULL,
  KEY `org_id` (`org_id`,`feature_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='stores entries which converts the purchase into features lis';


-- end  Schema : purchased_features