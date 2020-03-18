
-- start  Schema : user_list_trained_info

CREATE TABLE `user_list_trained_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `last_trained_at` datetime NOT NULL,
  `email_sent_at` datetime NOT NULL,
  `model_id` int(11) NOT NULL,
  `is_trained` tinyint(1) NOT NULL,
  `is_new_model` tinyint(1) NOT NULL,
  `is_warn_user` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : user_list_trained_info