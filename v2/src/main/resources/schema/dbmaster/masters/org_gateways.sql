
-- start  Schema : org_gateways

CREATE TABLE `org_gateways` (
  `org_id` int(11) NOT NULL,
  `gateway_id` int(11) NOT NULL,
  `sender_id` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `contact` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `priority` int(3) DEFAULT '-1',
  `credits` int(11) DEFAULT '0',
  PRIMARY KEY (`org_id`,`gateway_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : org_gateways