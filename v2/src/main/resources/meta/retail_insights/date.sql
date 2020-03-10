
-- start  Schema : date

CREATE TABLE `date` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `day_of_month` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `day_of_week` int(11) DEFAULT NULL,
  `week_number` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1461 DEFAULT CHARSET=latin1;


-- end  Schema : date