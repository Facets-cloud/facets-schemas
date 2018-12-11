package com.capillary.ops.bo.exceptions;

public class ResourceAlreadyExists extends RuntimeException {

    public ResourceAlreadyExists() {
        super();
    }

    public ResourceAlreadyExists(String message) {
        super(message);
    }
}
