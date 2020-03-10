
-- start  Schema : import_subscriptions

CREATE TABLE `import_subscriptions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(10) unsigned NOT NULL COMMENT 'if zero, subscribed to all orgs',
  `subscribed_from` date NOT NULL,
  `subscribed_upto` date NOT NULL,
  `added_by` int(10) unsigned NOT NULL,
  `added_on` datetime NOT NULL,
  `unsubscribed_on` datetime NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_subscriptions