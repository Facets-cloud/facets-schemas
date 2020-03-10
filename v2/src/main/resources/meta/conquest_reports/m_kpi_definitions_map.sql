
-- start  Schema : m_kpi_definitions_map

CREATE TABLE `m_kpi_definitions_map` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `kpi_id` char(32) DEFAULT NULL,
  `kpi_definition_id` int(10) unsigned DEFAULT NULL,
  `type` enum('STAGE','OUTPUT') DEFAULT NULL,
  `report_id` int(10) unsigned DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_updated_by` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


-- end  Schema : m_kpi_definitions_map