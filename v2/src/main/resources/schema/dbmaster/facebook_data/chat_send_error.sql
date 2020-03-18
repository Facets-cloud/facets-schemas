
-- start  Schema : chat_send_error

CREATE TABLE `chat_send_error` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chat_id` int(11) NOT NULL,
  `error_type` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `error_message` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- end  Schema : chat_send_error