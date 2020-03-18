
-- start  Schema : issue_tracker_escalation

CREATE TABLE `issue_tracker_escalation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `ticket_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `escalated_email` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_updated_on` datetime NOT NULL,
  `auto_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`org_id`),
  KEY `org_id` (`org_id`,`ticket_id`),
  KEY `org_time_idx` (`org_id`,`auto_update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : issue_tracker_escalation