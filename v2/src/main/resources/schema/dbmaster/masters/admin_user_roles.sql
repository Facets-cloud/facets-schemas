
-- start  Schema : admin_user_roles

CREATE TABLE `admin_user_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `admin_user_id` int(11) NOT NULL,
  `ref_id` int(11) NOT NULL COMMENT 'The id of the entity we are referring to. If The id is -1 it means the user has not been assigned any particular entity. .',
  `type` enum('ORG','ZONE','CONCEPT','STORE','TILL','STR_SERVER') COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(4) NOT NULL DEFAULT '1',
  `last_updated_by` int(11) DEFAULT NULL,
  `last_updated_on` datetime NOT NULL,
  `role_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`,`org_id`),
  UNIQUE KEY `org_id_4` (`org_id`,`admin_user_id`,`ref_id`,`role_id`),
  KEY `org_id_2` (`org_id`,`ref_id`,`type`),
  KEY `org_id_3` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : admin_user_roles