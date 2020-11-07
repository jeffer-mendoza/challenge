package com.merdadolibre.challenge.controller.v1;

import com.merdadolibre.challenge.exception.MessageNotDeterminedException;
import com.merdadolibre.challenge.exception.MissingInformationException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.service.challenge.IChallengeService;
import com.merdadolibre.challenge.utils.ConsUtil;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretSplitRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jefferson Mendoza, mendosajefferson@gmail.com
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/v1")
@Validated
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
      throws PositionNotDeterminedException, MessageNotDeterminedException {
    log.trace(ConsUtil.BEGIN_METHOD);
    TopSecretResponse topSecretResponse = challengeService.identifierMessage(request);
    log.trace(ConsUtil.END_METHOD);
    return new ResponseEntity<>(topSecretResponse, HttpStatus.OK);
  }

  /**
   * stores the information sent by a satellite.
   *
   * @param satelliteName satellite name
   * @param request request composed of distance and message
   * @param httpServletRequest http request to get the ip
   * @return http code confirming storage
   */
  @PostMapping(value = "/topsecret_split/{satellite_name}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TopSecretResponse> topSecretSplit(
      @PathVariable(value = "satellite_name") @NotBlank final String satelliteName,
      @Valid @RequestBody final TopSecretSplitRequest request, HttpServletRequest httpServletRequest) {
    log.trace(ConsUtil.BEGIN_METHOD);
    TopSecretResponse topSecretResponse = challengeService.saveInformation(satelliteName, request,
        httpServletRequest.getRemoteAddr());
    log.trace(ConsUtil.END_METHOD);
    return new ResponseEntity<>(topSecretResponse, HttpStatus.OK);
  }

  /**
   * retrieves the information from the satellites if possible.
   *
   * @param httpServletRequest http request to get the ip
   * @return return the information or error 404
   */
  @GetMapping(value = "/topsecret_split", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TopSecretResponse> topSecretSplit(HttpServletRequest httpServletRequest)
      throws PositionNotDeterminedException, MessageNotDeterminedException,
      MissingInformationException {
    log.trace(ConsUtil.BEGIN_METHOD);
    TopSecretResponse topSecretResponse = challengeService.getInformation(httpServletRequest.getRemoteAddr());
    log.trace(ConsUtil.END_METHOD);
    return new ResponseEntity<>(topSecretResponse, HttpStatus.OK);
  }
}

