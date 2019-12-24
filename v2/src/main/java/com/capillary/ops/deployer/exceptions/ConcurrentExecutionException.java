package com.capillary.ops.deployer.exceptions;

public class ConcurrentExecutionException extends RuntimeException {
    public ConcurrentExecutionException(String message) {
        super(message);
    }
}
