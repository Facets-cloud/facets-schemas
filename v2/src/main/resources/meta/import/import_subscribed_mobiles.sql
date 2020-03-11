
-- start  Schema : import_subscribed_mobiles

CREATE TABLE `import_subscribed_mobiles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_subscribe_id` int(10) unsigned NOT NULL,
  `mobile` varchar(13) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint(1) NOT NULL,
  `unsubscribed_on` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_subscribe_email` (`group_subscribe_id`,`mobile`,`status`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : import_subscribed_mobiles