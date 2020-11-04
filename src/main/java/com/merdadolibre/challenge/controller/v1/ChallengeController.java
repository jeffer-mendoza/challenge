package com.merdadolibre.challenge.controller.v1;

import com.merdadolibre.challenge.exception.DistanceNotDeterminedException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.service.challenge.IChallengeService;
import com.merdadolibre.challenge.utils.ConsUtil;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/v1")
public class ChallengeController {

  private IChallengeService challengeService;

  /**
   * Determine the position of the ship according to the points given.
   *
   * @param request list of messages received by satellites and distance from the ship
   * @return the position of the sending ship
   */
  @PostMapping(value = "/topsecret", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TopSecretResponse> topSecret(@Valid @RequestBody final TopSecretRequest request)
      throws DistanceNotDeterminedException, PositionNotDeterminedException {
    log.info(ConsUtil.BEGIN_METHOD);
    TopSecretResponse topSecretResponse = challengeService.identifierMessage(request);
    log.info(ConsUtil.END_METHOD);
    return new ResponseEntity<>(topSecretResponse, HttpStatus.OK);
  }
}

