package com.capillary.ops.deployer.exceptions;

public class NotFulfilledException extends RuntimeException {
    public NotFulfilledException(String message) {
        super(message);
    }
}
