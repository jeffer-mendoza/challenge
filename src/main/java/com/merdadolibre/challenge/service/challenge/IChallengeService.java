package com.merdadolibre.challenge.service.challenge;

import com.merdadolibre.challenge.exception.DistanceNotDeterminedException;
import com.merdadolibre.challenge.exception.MessageNotDeterminedException;
import com.merdadolibre.challenge.exception.MissingInformationException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretSplitRequest;

/**
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 */
public interface IChallengeService {

  TopSecretResponse identifierMessage(TopSecretRequest request)
      throws DistanceNotDeterminedException, PositionNotDeterminedException, MessageNotDeterminedException;

  TopSecretResponse saveInformation(String satelliteName, TopSecretSplitRequest request, String ipAddress);

  TopSecretResponse getInformation(String ipAddress) throws MissingInformationException, DistanceNotDeterminedException, PositionNotDeterminedException, MessageNotDeterminedException;
}
