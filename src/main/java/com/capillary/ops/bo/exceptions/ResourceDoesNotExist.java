package com.capillary.ops.bo.exceptions;

public class ResourceDoesNotExist extends RuntimeException{

    public ResourceDoesNotExist() {
        super();
    }

    public ResourceDoesNotExist(String message) {
        super(message);
    }
}
