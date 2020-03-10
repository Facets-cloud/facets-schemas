
-- start  Schema : org_source_attribute_priority

CREATE TABLE `org_source_attribute_priority` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `attribute_type` enum('COMM_CHANNEL','IDENTIFIER','EXTENDED_FIELD') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'COMM_CHANNEL',
  `attribute_id` int(2) NOT NULL COMMENT 'Key of `source_comm_channel_type`/`source_identifier_type`/`source_extended_field_type`',
  `org_source_id` int(11) NOT NULL COMMENT 'Key of `org_source`',
  `priority` int(2) NOT NULL COMMENT 'Priority across org-sources',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueAttributePriority` (`attribute_type`,`attribute_id`,`org_source_id`,`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='Sets priority among attribute-types across org-sources';


-- end  Schema : org_source_attribute_priority