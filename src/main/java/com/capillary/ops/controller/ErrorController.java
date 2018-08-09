package com.capillary.ops.controller;

import com.capillary.ops.bo.ErrorDetails;
import com.capillary.ops.bo.exceptions.ApplicationAlreadyExists;
import com.capillary.ops.bo.exceptions.ApplicationDoesNotExist;
import com.capillary.ops.bo.exceptions.DeploymentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(ApplicationAlreadyExists.class)
    public ResponseEntity<ErrorDetails> applicationAlreadyExists(ApplicationAlreadyExists ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails("Application already exists", "ERR01"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationDoesNotExist.class)
    public ResponseEntity<ErrorDetails> applicationDoesNotExist(ApplicationDoesNotExist ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails("Application does not exist", "ERR02"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeploymentNotFoundException.class)
    public ResponseEntity<ErrorDetails> deploymentDoesNotExist(DeploymentNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails("Deployment does not exist", "ERR03"), HttpStatus.NOT_FOUND);
    }

}
