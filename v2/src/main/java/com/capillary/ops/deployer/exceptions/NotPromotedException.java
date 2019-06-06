package com.capillary.ops.deployer.exceptions;

public class NotPromotedException extends RuntimeException {
    public NotPromotedException(String message) {
        super(message);
    }
}
