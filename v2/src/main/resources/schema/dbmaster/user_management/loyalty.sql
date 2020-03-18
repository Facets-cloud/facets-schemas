
-- start  Schema : loyalty

CREATE TABLE `loyalty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `publisher_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `loyalty_points` mediumint(9) NOT NULL,
  `slab_number` int(11) DEFAULT NULL,
  `slab_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lifetime_points` bigint(20) NOT NULL DEFAULT '0',
  `lifetime_purchases` bigint(20) NOT NULL DEFAULT '0',
  `external_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'an external identifier that can be used by the client',
  `joined` datetime NOT NULL,
  `registered_by` bigint(20) NOT NULL COMMENT 'User ID who registered this loyalty account',
  `last_updated_by` bigint(20) NOT NULL,
  `last_updated` datetime NOT NULL,
  `last_statement_sent` datetime DEFAULT NULL COMMENT 'Date-time when the last monthly loyalty statement was sent',
  `counter_id` bigint(20) NOT NULL DEFAULT '-1',
  `base_store` int(11) NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `loyalty_status` enum('NORMAL','INTERNAL','FRAUD','TEST','OTHER','OTHER2','OTHER3') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NORMAL',
  `type` enum('loyalty','non_loyalty') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'loyalty',
  `source` enum('instore','e-comm','newsletter','campaigns','NCA','WECHAT','MARTJACK','WEB_ENGAGE','FACEBOOK','TMALL','OTHERS','WEBSITE','TAOBAO','JD','ECOMMERCE','KAOLA','PINDUODUO','SUNING','GLOBAL_SCANNER','XIAOHONGSHU') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'instore',
  `conversion_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`publisher_id`) USING BTREE,
  UNIQUE KEY `publisher_id` (`publisher_id`,`user_id`),
  UNIQUE KEY `external_id` (`publisher_id`,`external_id`),
  KEY `publisher_id_2` (`publisher_id`,`registered_by`),
  KEY `publisher_id_3` (`publisher_id`,`joined`),
  KEY `joined` (`joined`),
  KEY `auto_time_idx` (`auto_update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : loyalty