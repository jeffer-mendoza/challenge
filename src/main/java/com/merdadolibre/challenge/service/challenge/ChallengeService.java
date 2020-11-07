package com.merdadolibre.challenge.service.challenge;

import com.merdadolibre.challenge.configuration.properties.MainProperties;
import com.merdadolibre.challenge.exception.MessageNotDeterminedException;
import com.merdadolibre.challenge.exception.MissingInformationException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.model.Vector;
import com.merdadolibre.challenge.utils.ConsUtil;
import com.merdadolibre.challenge.utils.TrilateralationUtil;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretSplitRequest;
import com.merdadolibre.dto.challenge.external.topsecret.model.Position;
import com.merdadolibre.dto.challenge.external.topsecret.model.Satellite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Setter;
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

  private static final int QUANTITY_SATELLITE = 3;
  private static final int SATELLITE_0 = 0;
  private static final int SATELLITE_1 = 1;
  private static final int SATELLITE_2 = 2;

  @Setter private Map<String,TopSecretRequest> cache;
  private MainProperties config;
  private  Map<String, Integer>  satellitesOrder;

  /**
   * initialize the position of the satellites.
   */
  @PostConstruct
  public void init() {
    int index = 0;
    satellitesOrder = new HashMap<>();
    for (Vector satellite : config.getSatellites()) {
      satellitesOrder.put(satellite.getName(), index);
      index++;
    }
  }

  @Override
  public TopSecretResponse identifierMessage(final TopSecretRequest request) throws PositionNotDeterminedException,
      MessageNotDeterminedException {
    log.trace(ConsUtil.BEGIN_METHOD);
    float[] distances = new float[QUANTITY_SATELLITE];
    List<String[]> messages = new ArrayList<>();
    Integer index;
    for (Satellite satellite : request.getSatellites()) {
      index = satellitesOrder.getOrDefault(satellite.getName(), null);
      if (index != null) {
        distances[index] = satellite.getDistance();
        messages.add(satellite.getMessage());
      }
    }
    final Vector shipPosition = getLocation(distances);
    final Position position = Position.builder()
        .x((float)shipPosition.getPositionx()).y((float)shipPosition.getPositiony()).build();
    TopSecretResponse topSecretResponse = TopSecretResponse.builder().position(position)
        .message(getMessage(messages.toArray(new String[messages.size()][0])))
        .build();
    log.trace(ConsUtil.END_METHOD);
    return topSecretResponse;
  }

  @Override
  public TopSecretResponse saveInformation(final String satelliteName, final TopSecretSplitRequest request,
                                           final String ipAddress) {
    log.trace(ConsUtil.BEGIN_METHOD);
    TopSecretRequest topSecretRequest = cache.get(ipAddress);
    if (Objects.isNull(topSecretRequest)) {
      topSecretRequest = new TopSecretRequest();
    }
    final Satellite satelliteNew = Satellite.builder().distance(request.getDistance())
        .message(request.getMessage()).name(satelliteName).build();
    if (topSecretRequest.getSatellites() == null) {
      topSecretRequest.setSatellites(new ArrayList<>());
    } else {
      topSecretRequest.getSatellites().remove(satelliteNew);
    }
    topSecretRequest.getSatellites().add(satelliteNew);
    cache.put(ipAddress, topSecretRequest);
    log.trace(ConsUtil.END_METHOD);
    return TopSecretResponse.builder().okMessage(ConsUtil.MESSAGE_OK).build();
  }

  @Override
  public TopSecretResponse getInformation(String ipAddress) throws MissingInformationException,
      PositionNotDeterminedException, MessageNotDeterminedException {
    log.trace(ConsUtil.BEGIN_METHOD);
    TopSecretRequest topSecretRequest = cache.get(ipAddress);
    if (Objects.isNull(topSecretRequest) || topSecretRequest.getSatellites().size() != 3) {
      throw new MissingInformationException(ConsUtil.MISSING_INFORMATION);
    }
    log.trace(ConsUtil.END_METHOD);
    return identifierMessage(topSecretRequest);
  }

  /**
   * get ship position.
   *
   * @param distances distances between satellites and spacecraft
   * @return vector position
   * @throws PositionNotDeterminedException if no solution is found to the system of equations
   */
  private Vector getLocation(float ...distances) throws PositionNotDeterminedException {
    return TrilateralationUtil.determinePosition(config.getSatellites().get(SATELLITE_0),
        config.getSatellites().get(SATELLITE_1), config.getSatellites().get(SATELLITE_2), distances,
        config.getToleranceError());
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
        if (Strings.isNotBlank(messageSatellite[i])) {
          temp = messageSatellite[i];
        }
      }
      finalMessage[i] = temp;
    }
    return String.join(" ", finalMessage).trim();
  }

}
