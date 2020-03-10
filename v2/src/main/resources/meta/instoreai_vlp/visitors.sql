
-- start  Schema : visitors

CREATE TABLE `visitors` (
  `User__user_id` int(20) NOT NULL,
  `org_id` int(15) NOT NULL,
  `User__first_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `User__last_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `User__mobile` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `User__email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Last_purchased_store__store_id` int(20) DEFAULT NULL,
  `Last_purchased_store__Till_Id` int(20) DEFAULT NULL,
  `Auto_Update_Time_Cps` int(20) DEFAULT NULL,
  `Auto_Update_Time_Extnd` int(20) DEFAULT NULL,
  `is_registered` tinyint(1) NOT NULL DEFAULT '0',
  `registration_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Not Started',
  `imageQuality` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ReckoMsg` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code` int(10) DEFAULT NULL,
  `ImageRegistered` int(10) DEFAULT '0',
  `Date__Date` date DEFAULT NULL,
  `Time__Time` time DEFAULT NULL,
  `Last_purchased_store__till_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`User__user_id`),
  KEY `is_registered` (`is_registered`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : visitors