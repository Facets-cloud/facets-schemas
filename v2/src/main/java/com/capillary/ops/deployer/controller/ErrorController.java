package com.capillary.ops.deployer.controller;

import com.capillary.ops.cp.exceptions.BadRequestException;
import com.capillary.ops.deployer.bo.ErrorDetails;
import com.capillary.ops.deployer.exceptions.NoSuchInfrastructureResourceException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import com.github.alturkovic.lock.exception.LockNotAvailableException;
import com.mongodb.MongoWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.capillary.ops.cp.exceptions.ProdReleaseDisabled;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@ControllerAdvice
public class ErrorController {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationFacade.class);

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorDetails> applicationAlreadyExists(
          NoSuchElementException ex, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorDetails("Requested resource not found", "404"), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorDetails> notFoundException(
          NotFoundException ex, WebRequest request) {
    String message = ex.getMessage() == null ? "Requested resource not found" : ex.getMessage();
    return new ResponseEntity<>(
        new ErrorDetails(message, "404"), HttpStatus.NOT_FOUND);
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
    logger.error("Internal Server Error", ex);
    return new ResponseEntity<>(
            new ErrorDetails(ex.getMessage(), "500"), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
  @ExceptionHandler(NoHandlerFoundException.class)
  public ModelAndView handleNoHandlerFoundException() {
    return new ModelAndView("/");
  }

  @ExceptionHandler(NoSuchInfrastructureResourceException.class)
  public ResponseEntity<Map<String, String>> missingInfraResourceInstance(
          NoSuchInfrastructureResourceException ex, WebRequest request) {
    return new ResponseEntity<>(
            new HashMap<>(), HttpStatus.OK);
  }

  @ExceptionHandler(ProdReleaseDisabled.class)
  public ResponseEntity<ErrorDetails> prodReleaseDisabledException(ProdReleaseDisabled ex, WebRequest request) {
    String message = Optional.ofNullable(ex.getMessage()).orElse("Prod Release is Disabled for the given stack");
    return new ResponseEntity<>(new ErrorDetails(message, "403"), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorDetails> badRequestException(BadRequestException ex, WebRequest request) {
    return new ResponseEntity<>(new ErrorDetails(ex.getMessage(), "400"),
            HttpStatus.BAD_REQUEST);
  }
}
