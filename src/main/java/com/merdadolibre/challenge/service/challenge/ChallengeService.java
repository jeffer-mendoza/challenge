package com.merdadolibre.challenge.service.challenge;

import com.merdadolibre.challenge.configuration.properties.MainProperties;
import com.merdadolibre.challenge.exception.DistanceNotDeterminedException;
import com.merdadolibre.challenge.exception.MessageNotDeterminedException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.model.Vector;
import com.merdadolibre.challenge.utils.ConsUtil;
import com.merdadolibre.challenge.utils.TrilateralationUtil;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import com.merdadolibre.dto.challenge.external.topsecret.model.Position;
import com.merdadolibre.dto.challenge.external.topsecret.model.Satellite;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

/**
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 */
@Slf4j
@AllArgsConstructor
@Service
public class ChallengeService implements IChallengeService {

  private MainProperties config;

  private static final int QUANTITY_SATELLITE = 3;
  private static final int SATELLITE_0 = 0;
  private static final int SATELLITE_1 = 1;
  private static final int SATELLITE_2 = 2;

  @Override
  public TopSecretResponse identifierMessage(TopSecretRequest request) throws DistanceNotDeterminedException,
      PositionNotDeterminedException, MessageNotDeterminedException {
    log.info(ConsUtil.BEGIN_METHOD);
    TopSecretResponse topSecretResponse;
    float[] distances = new float[QUANTITY_SATELLITE];
    List<String[]> messages = new ArrayList<>();
    int index;
    for (Satellite satellite : request.getSatellites()) {
      index = getIndex(satellite.getName(), config.getSatellites());
      distances[index] = satellite.getDistance();
      messages.add(satellite.getMessage());
    }
    final Vector shipPosition = getLocation(distances);
    final Position position = Position.builder()
        .x((float)shipPosition.getPositionx()).y((float)shipPosition.getPositiony()).build();
    topSecretResponse = TopSecretResponse.builder().position(position)
        .message(getMessage(messages.toArray(new String[messages.size()][0])))
        .build();
    log.info(ConsUtil.END_METHOD);
    return topSecretResponse;
  }

  /**
   * get ship position.
   *
   * @param distances distances between satellites and spacecraft
   * @return vector position
   * @throws PositionNotDeterminedException if no solution is found to the system of equations
   */
  private Vector getLocation(float ...distances) throws PositionNotDeterminedException {
    final Vector shipPosition =  TrilateralationUtil.determinePosition(config.getSatellites().get(SATELLITE_0),
        config.getSatellites().get(SATELLITE_1), config.getSatellites().get(SATELLITE_2), distances);
    if (shipPosition == null) {
      log.warn(ConsUtil.POSITION_IS_NULL);
      throw new PositionNotDeterminedException(ConsUtil.POSITION_NOT_DETERMINED);
    } else {
      return shipPosition;
    }
  }


  /**
   * get the message from the ships.
   * @param messages message array list
   * @return the composition of the message
   */
  protected String getMessage(String[] ...messages) throws MessageNotDeterminedException {
    final int size = messages[0].length;
    if (size == 0 || size != messages[1].length || size != messages[2].length) {
      throw new MessageNotDeterminedException(ConsUtil.MESSAGE_NOT_DETERMINED);
    }
    String[] finalMessage = new String[size];
    String temp;
    for (int i = 0; i < size; i++) {
      temp = Strings.EMPTY;
      for (String[] messageSatellite : messages) {
        if (messageSatellite.length > i && (Strings.isNotBlank(messageSatellite[i]))) {
          temp = messageSatellite[i];
        }
      }
      finalMessage[i] = temp;
    }
    return String.join(" ", finalMessage);
  }

  /**
   * Get index of array.
   *
   * @return return the index for configured the array of distances
   * @throws DistanceNotDeterminedException if the satellite information is not correct
   */
  private int getIndex(final String satelliteName, final List<Vector> satellites)
      throws DistanceNotDeterminedException {
    log.info(ConsUtil.BEGIN_METHOD);
    for (int i = 0; i < satellites.size(); i++) {
      if (satellites.get(i).getName().equalsIgnoreCase(satelliteName)) {
        return i;
      }
    }
    throw new DistanceNotDeterminedException(ConsUtil.DISTANCE_NOT_DETERMINED);
  }
}
