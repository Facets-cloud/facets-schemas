
-- start  Schema : transaction_delivery_status

CREATE TABLE `transaction_delivery_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key ',
  `transaction_id` int(11) NOT NULL DEFAULT '-1' COMMENT 'Retro-request ID (Foreign Key From loyalty_log, returned_bills, loyalty_not_interested_bills, not_interested_return_bill_lineitems)',
  `transaction_type` enum('REGULAR','RETURN','NOT_INTERESTED','NOT_INTERESTED_RETURN') NOT NULL DEFAULT 'REGULAR',
  `delivery_status` enum('PLACED','PROCESSED','SHIPPED','DELIVERED','RETURNED') NOT NULL DEFAULT 'DELIVERED',
  `updated_by` int(11) NOT NULL DEFAULT '-1' COMMENT 'ID Of the status updator',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueTransactionIdAndType` (`transaction_id`,`transaction_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPRESSED KEY_BLOCK_SIZE=16 COMMENT='This table records the delivery_status of a transaction (1:1)';


-- end  Schema : transaction_delivery_status