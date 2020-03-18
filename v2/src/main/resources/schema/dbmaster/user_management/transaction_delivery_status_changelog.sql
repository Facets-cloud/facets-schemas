
-- start  Schema : transaction_delivery_status_changelog

CREATE TABLE `transaction_delivery_status_changelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key ',
  `transaction_id` int(11) NOT NULL DEFAULT '-1' COMMENT 'Retro-request ID (Foreign Key From loyalty_log, returned_bills, loyalty_not_interested_bills, not_interested_return_bill_lineitems)',
  `transaction_type` enum('REGULAR','RETURN','NOT_INTERESTED','NOT_INTERESTED_RETURN') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'REGULAR' COMMENT 'Transaction type: REGULAR = loyalty_log, RETURN = returned_bills, NOT_INTERESTED = loyalty_not_interested_bills, NOT_INTERESTED_RETURN = not_interested_return_bills',
  `delivery_status` enum('PLACED','PROCESSED','SHIPPED','DELIVERED','RETURNED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DELIVERED' COMMENT 'E-commerce/store order''s delivery status',
  `updated_by` int(11) NOT NULL DEFAULT '-1' COMMENT 'ID Of the status updator',
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Time when the status was updated',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='This table records of all the changes made to the delivery_status of a transaction (1:M)';


-- end  Schema : transaction_delivery_status_changelog