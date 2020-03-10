
-- start  Schema : response_time

CREATE TABLE `response_time` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `day` date NOT NULL,
  `hour` int(11) NOT NULL,
  `client` varchar(20) NOT NULL,
  `api_name` varchar(100) NOT NULL,
  `hit_count` int(11) NOT NULL,
  `time_in_miliseconds` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hourly_api` (`day`,`hour`,`client`,`api_name`)
) ENGINE=InnoDB AUTO_INCREMENT=25113 DEFAULT CHARSET=latin1;


-- end  Schema : response_time