package com.merdadolibre.challenge.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 * @since 1.0
 */
@RestController
@RequestMapping("/")
public class BaseController {

  /**
   * retrieves the information from the satellites if possible.
   *
   * @return ok
   */
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> init() {
    return new ResponseEntity<>("ok", HttpStatus.OK);
  }
}
