
-- start  Schema : notification_sent_logs

CREATE TABLE `notification_sent_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `receiver_group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `action_ids` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_id` int(11) NOT NULL,
  `nsadmin_id` bigint(20) NOT NULL,
  `sent_date` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `org_user_ref_time_index` (`org_id`,`user_id`,`ref_id`,`sent_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : notification_sent_logs