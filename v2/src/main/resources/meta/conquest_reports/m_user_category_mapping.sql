
-- start  Schema : m_user_category_mapping

CREATE TABLE `m_user_category_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `is_template_mapping` tinyint(1) NOT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


-- end  Schema : m_user_category_mapping