
-- start  Schema : banding_range

CREATE TABLE `banding_range` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `band_id` int(11) NOT NULL,
  `ordinal_numbuer` int(11) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` double DEFAULT NULL,
  `scope_id` int(11) NOT NULL DEFAULT '-1000',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


-- end  Schema : banding_range