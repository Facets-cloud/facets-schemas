
-- start  Schema : async_jobs

CREATE TABLE `async_jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `submitted_by` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Who has submitted the job',
  `processed_by` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Name of the consumer',
  `submission_time` datetime DEFAULT NULL COMMENT 'what time was the job submitted in queue',
  `picked_time` datetime DEFAULT NULL COMMENT 'what time the job was picked by a consumer',
  `finished_time` datetime DEFAULT NULL COMMENT 'time when the consumer finished processing the job',
  `context` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'dump of the context map in the job',
  `payload` mediumtext COLLATE utf8mb4_unicode_ci COMMENT 'dump of the payload of the job',
  `job_id` int(11) DEFAULT NULL COMMENT 'id allocated to this job by beanstalk',
  `status` enum('DELETE','ARCHIVE','RESCHEDULE') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `scheduled_job_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16;


-- end  Schema : async_jobs