package com.capillary.ops.deployer.exceptions;

public class InvalidScheduleException extends RuntimeException {
    public InvalidScheduleException(String message) {
        super(message);
    }
}
