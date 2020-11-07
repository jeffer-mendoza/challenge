package com.merdadolibre.challenge.handler;

import com.merdadolibre.challenge.exception.MessageNotDeterminedException;
import com.merdadolibre.challenge.exception.MissingInformationException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Jefferson Mendoza, mendosajefferson@gmail.com
 */
@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

  /**
   * handles business logic exceptions.
   * @param ex exception
   * @return http response with error message
   */
  @ExceptionHandler(value = {PositionNotDeterminedException.class, MessageNotDeterminedException.class,
      MissingInformationException.class})
  public ResponseEntity<Object> handleBusinessException(Exception ex) {
    TopSecretResponse topSecretResponse = TopSecretResponse.builder().errorMessage(ex.getMessage()).build();
    return new ResponseEntity<>(topSecretResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * handles bad request exceptions.
   * @param ex invalid argument exception
   * @return http response with error message
   */
  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * handles bad request exceptions.
   * @param ex invalid argument exception
   * @return http response with error message
   */
  @ExceptionHandler(value = {ConstraintViolationException.class})
  public ResponseEntity<Object> handleValidationPath(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations().forEach(error -> {
      errors.put("satelliteName", error.getMessage());
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * default exception.
   * @param ex exception
   * @return http response with error message
   */
  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<Object> defaultException(Exception ex) {
    log.error("Exception Default", ex);
    TopSecretResponse response = TopSecretResponse.builder().errorMessage("An error has occurred").build();
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
