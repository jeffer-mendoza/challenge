package com.merdadolibre.challenge.service.challenge;

import com.merdadolibre.challenge.configuration.properties.MainProperties;
import com.merdadolibre.challenge.utils.ConsUtil;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 */
@Slf4j
@AllArgsConstructor
@Service
public class ChallengeService implements IChallengeService {

  private MainProperties config;

  @Override
  public TopSecretResponse identifierMessage(TopSecretRequest request) {
    log.info(ConsUtil.BEGIN_METHOD);
    TopSecretResponse topSecretResponse = new TopSecretResponse();
    // TODO hacer la logica para retonar las naves
    log.info(ConsUtil.END_METHOD);
    return topSecretResponse;
  }
}
