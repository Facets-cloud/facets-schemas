package com.capillary.ops.deployer.exceptions;

public class InvalidSecretException extends RuntimeException {
    public InvalidSecretException(String message) {
        super(message);
    }
}
