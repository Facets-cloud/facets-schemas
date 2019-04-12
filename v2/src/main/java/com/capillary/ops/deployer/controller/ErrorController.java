package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

@RestController
@ControllerAdvice
public class ErrorController {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorDetails> applicationAlreadyExists(
          NoSuchElementException ex, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorDetails("Requested resource not found", "404"), HttpStatus.NOT_FOUND);
  }
}
