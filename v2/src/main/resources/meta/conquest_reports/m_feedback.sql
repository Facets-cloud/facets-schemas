
-- start  Schema : m_feedback

CREATE TABLE `m_feedback` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(10) NOT NULL,
  `user_login` char(64) NOT NULL,
  `brand` varchar(64) NOT NULL,
  `user_type` enum('CAP','NON_CAP','SUPERADMIN') NOT NULL,
  `access_type` enum('STORE','ZONE','CONCEPT','ORG') NOT NULL,
  `category` varchar(64) NOT NULL,
  `feedback` text CHARACTER SET utf8 NOT NULL,
  `browser` varchar(64) DEFAULT NULL,
  `browser_version` varchar(32) DEFAULT NULL,
  `os` varchar(64) DEFAULT NULL,
  `device` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;


-- end  Schema : m_feedback