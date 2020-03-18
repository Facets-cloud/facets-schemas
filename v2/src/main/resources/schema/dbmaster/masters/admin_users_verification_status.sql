
-- start  Schema : admin_users_verification_status

CREATE TABLE `admin_users_verification_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` enum('WHITELISTING','REJECTED_BY_WHITELISTING','WHITELISTED','CAP_POC_AUTHORIZED','CAP_POC_REJECTED','BRAND_POC_AUTHORIZED','BRAND_POC_REJECTED','ACTIVE','DEACTIVATED') DEFAULT NULL,
  `description` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;


-- end  Schema : admin_users_verification_status