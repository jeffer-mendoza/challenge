package com.merdadolibre.challenge.service.challenge;

import com.merdadolibre.challenge.configuration.properties.MainProperties;
import com.merdadolibre.challenge.exception.DistanceNotDeterminedException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.model.Vector;
import com.merdadolibre.challenge.utils.ConsUtil;
import com.merdadolibre.challenge.utils.TrilateralationUtil;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import com.merdadolibre.dto.challenge.external.topsecret.model.Position;
import com.merdadolibre.dto.challenge.external.topsecret.model.Satellite;
import java.util.List;
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
  public TopSecretResponse identifierMessage(TopSecretRequest request) throws DistanceNotDeterminedException,
      PositionNotDeterminedException {
    log.info(ConsUtil.BEGIN_METHOD);
    TopSecretResponse topSecretResponse;
    float[] distances = new float[3];
    int index = 0;
    for (Vector satellite: config.getSatellites()) {
      distances[index] = getDistance(satellite.getName(), request.getSatellites());
      index++;
    }
    final Vector shipPosition =  TrilateralationUtil.determinePosition(
        config.getSatellites().get(0), config.getSatellites().get(1), config.getSatellites().get(2), distances);
    if (shipPosition == null) {
      log.warn(ConsUtil.POSITION_IS_NULL);
      throw new PositionNotDeterminedException(ConsUtil.POSITION_NOT_DETERMINED);
    } else {
      final Position position = Position.builder()
          .x((float)shipPosition.getPositionx()).y((float)shipPosition.getPositiony()).build();
      topSecretResponse = TopSecretResponse.builder().position(position).message("").build();
    }
    log.info(ConsUtil.END_METHOD);
    return topSecretResponse;
  }

  /**
   * Get the distance between ship and satellite.
   *
   * @return return the distance of the given satellite
   * @throws DistanceNotDeterminedException if the satellite information and the distance sent is not correct
   */
  private float getDistance(final String satelliteName, final List<Satellite> satellites)
      throws DistanceNotDeterminedException {
    log.info(ConsUtil.BEGIN_METHOD);
    Satellite satelliteResult = satellites.stream()
        .filter(satellite -> satelliteName.equalsIgnoreCase(satellite.getName().trim()))
        .findFirst().orElse(null);
    if (satelliteResult == null || satelliteResult.getDistance() < 0) {
      throw new DistanceNotDeterminedException(ConsUtil.DISTANCE_NOT_DETERMINED);
    } else {
      return satelliteResult.getDistance();
    }
  }
}
