package com.merdadolibre.challenge.handler;

import com.merdadolibre.challenge.exception.DistanceNotDeterminedException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {DistanceNotDeterminedException.class, PositionNotDeterminedException.class})
  public ResponseEntity<Object> handleBusinessException(Exception ex, WebRequest request) {
    TopSecretResponse topSecretResponse = TopSecretResponse.builder().errorMessage(ex.getMessage()).build();
    return new ResponseEntity<>(topSecretResponse, HttpStatus.NOT_FOUND);
  }
}
