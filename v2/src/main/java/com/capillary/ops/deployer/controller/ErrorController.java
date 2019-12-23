package com.capillary.ops.deployer.controller;

import com.capillary.ops.deployer.bo.ErrorDetails;
import com.github.alturkovic.lock.exception.LockNotAvailableException;
import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
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

  @ExceptionHandler(MongoWriteException.class)
  public ResponseEntity<ErrorDetails> alreadyExists(
          MongoWriteException ex, WebRequest request) {
    return new ResponseEntity<>(
            new ErrorDetails(ex.getMessage(), "400"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> applicationAlreadyExists(
          Exception ex, WebRequest request) throws Exception {
    throw ex;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
  @ExceptionHandler(NoHandlerFoundException.class)
  public ModelAndView handleNoHandlerFoundException() {
    return new ModelAndView("/");
  }
}
