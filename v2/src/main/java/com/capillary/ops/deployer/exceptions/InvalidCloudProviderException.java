package com.capillary.ops.deployer.exceptions;

public class InvalidCloudProviderException extends RuntimeException {
    public InvalidCloudProviderException(String message) {
        super(message);
    }
}
