package com.capillary.ops.cp.exceptions;

public class ClusterDetailsNotFound extends RuntimeException{
    public ClusterDetailsNotFound(){
        super("could not fetch cluster details. deployment context not found");
    }
}
