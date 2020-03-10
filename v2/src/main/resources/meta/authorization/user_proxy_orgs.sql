
-- start  Schema : user_proxy_orgs

CREATE TABLE `user_proxy_orgs` (
  `user_id` bigint(20) NOT NULL,
  `proxy_org_id` bigint(20) NOT NULL,
  `active` tinyint(4) NOT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `modified_on` datetime DEFAULT NULL,
  UNIQUE KEY `user_id` (`user_id`,`proxy_org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : user_proxy_orgs