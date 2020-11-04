package com.merdadolibre.challenge.service.challenge;

import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;

/**
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 */
public interface IChallengeService {
  TopSecretResponse identifierMessage(TopSecretRequest request);
}
