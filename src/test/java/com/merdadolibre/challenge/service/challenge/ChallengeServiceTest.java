package com.merdadolibre.challenge.service.challenge;

import com.merdadolibre.challenge.BaseTest;
import com.merdadolibre.challenge.exception.MessageNotDeterminedException;
import com.merdadolibre.challenge.exception.MissingInformationException;
import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.utils.ConsUtil;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretRequest;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretResponse;
import com.merdadolibre.dto.challenge.external.topsecret.TopSecretSplitRequest;
import com.merdadolibre.dto.challenge.external.topsecret.model.Position;
import com.merdadolibre.dto.challenge.external.topsecret.model.Satellite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeServiceTest extends BaseTest {

  private static final String KENOBI = "kenobi";
  private static final String SKYWALKER = "skywalker";
  private static final String SATO = "sato";
  private static final String UNKNOWN = "unknown";

  private static final float DISTANCE_TO_KENOBI = 485.69f;
  private static final float DISTANCE_TO_SKYWALKER = 266.083f;
  private static final float DISTANCE_TO_SATO = 600.5f;

  private static final String IP_ADDRESS = "10.0.0.12";

  @Autowired
  private ChallengeService challengeService;

  private String[] messageS1;
  private String[] messageS2;
  private String[] messageS3;

  private TopSecretRequest topSecretRequest;
  private List<Satellite> satelliteList;

  @BeforeEach
  public void septup() {
    satelliteList = new ArrayList<>();
    topSecretRequest = new TopSecretRequest();

    messageS1 = new String[]{"este", "", "", "mensaje", ""};
    messageS2 = new String[]{"", "es", "", "mensaje", ""};
    messageS3 = new String[]{"este", "", "un", "mensaje", ""};
    challengeService.setCache(new HashMap<>());
  }

  /** identifier Message **/
  @Test
  void identifierMessageTest_positionFound() throws PositionNotDeterminedException, MessageNotDeterminedException {
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_KENOBI).name(KENOBI).message(messageS1).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SKYWALKER).name(SKYWALKER).message(messageS2).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SATO).name(SATO).message(messageS3).build());
    topSecretRequest.setSatellites(satelliteList);
    TopSecretResponse response = challengeService.identifierMessage(topSecretRequest);
    Position positionExpected  =  Position.builder().x(-100).y(75.5f).build();
    assertEquals(positionExpected, response.getPosition());
    assertEquals("este es un mensaje", response.getMessage());
  }

  @Test
  void identifierMessageTest_positionFoundWithExtraSatellite() throws PositionNotDeterminedException, MessageNotDeterminedException {
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_KENOBI).name(KENOBI).message(messageS1).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SKYWALKER).name(SKYWALKER).message(messageS2).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SATO).name(SATO).message(messageS3).build());
    satelliteList.add(Satellite.builder().distance(60f).name(UNKNOWN).message(messageS3).build());
    topSecretRequest.setSatellites(satelliteList);
    TopSecretResponse response = challengeService.identifierMessage(topSecretRequest);
    Position positionExpected  =  Position.builder().x(-100).y(75.5f).build();
    assertEquals(positionExpected, response.getPosition());
    assertEquals("este es un mensaje", response.getMessage());
  }

  @Test
  void identifierMessageTest_positionNotFound() {
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_KENOBI).name(KENOBI).message(messageS1).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SKYWALKER).name(SKYWALKER).message(messageS2).build());
    satelliteList.add(Satellite.builder().distance(0f).name(SATO).message(messageS3).build());
    topSecretRequest.setSatellites(satelliteList);
    Exception exception = assertThrows(PositionNotDeterminedException.class, () -> {
      challengeService.identifierMessage(topSecretRequest);
    });
    String expectedMessage = ConsUtil.POSITION_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void identifierMessageTest_messageNotFoundOfSatellite3() {
    messageS3 = new String[]{"un", "mensaje", ""};
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_KENOBI).name(KENOBI).message(messageS1).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SKYWALKER).name(SKYWALKER).message(messageS2).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SATO).name(SATO).message(messageS3).build());
    topSecretRequest.setSatellites(satelliteList);
    Exception exception = assertThrows(MessageNotDeterminedException.class, () -> {
      challengeService.identifierMessage(topSecretRequest);
    });
    String expectedMessage = ConsUtil.MESSAGE_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void identifierMessageTest_messageNotFoundOfSatellite2() {
    messageS2 = new String[]{"un", "mensaje", ""};
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_KENOBI).name(KENOBI).message(messageS1).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SKYWALKER).name(SKYWALKER).message(messageS2).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SATO).name(SATO).message(messageS3).build());
    topSecretRequest.setSatellites(satelliteList);
    Exception exception = assertThrows(MessageNotDeterminedException.class, () -> {
      challengeService.identifierMessage(topSecretRequest);
    });
    String expectedMessage = ConsUtil.MESSAGE_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void identifierMessageTest_messageNotFoundOfSatellite1() {
    messageS1 = new String[]{"un", "mensaje", ""};
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_KENOBI).name(KENOBI).message(messageS1).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SKYWALKER).name(SKYWALKER).message(messageS2).build());
    satelliteList.add(Satellite.builder().distance(DISTANCE_TO_SATO).name(SATO).message(messageS3).build());
    topSecretRequest.setSatellites(satelliteList);
    Exception exception = assertThrows(MessageNotDeterminedException.class, () -> {
      challengeService.identifierMessage(topSecretRequest);
    });
    String expectedMessage = ConsUtil.MESSAGE_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  /* saveInformation */
  @Test
  void saveInformationTest_successForKenobi() {
    TopSecretSplitRequest request =
        TopSecretSplitRequest.builder().distance(DISTANCE_TO_KENOBI).message(messageS1).build();
    TopSecretResponse topSecretResponse = challengeService.saveInformation(KENOBI, request, IP_ADDRESS);
    assertEquals(ConsUtil.MESSAGE_OK, topSecretResponse.getOkMessage());
  }

  @Test
  void saveInformationTest_successForSkywalker() {
    TopSecretSplitRequest request =
        TopSecretSplitRequest.builder().distance(DISTANCE_TO_SKYWALKER).message(messageS2).build();
    TopSecretResponse topSecretResponse = challengeService.saveInformation(SKYWALKER, request, IP_ADDRESS);
    assertEquals(ConsUtil.MESSAGE_OK, topSecretResponse.getOkMessage());
  }

  @Test
  void saveInformationTest_successForSato() {
    TopSecretSplitRequest request =
        TopSecretSplitRequest.builder().distance(DISTANCE_TO_SATO).message(messageS3).build();
    TopSecretResponse topSecretResponse = challengeService.saveInformation(SATO, request, IP_ADDRESS);
    assertEquals(ConsUtil.MESSAGE_OK, topSecretResponse.getOkMessage());
  }

  @Test
  void saveInformationTest_overrideSato() {
    TopSecretSplitRequest request =
        TopSecretSplitRequest.builder().distance(DISTANCE_TO_SATO).message(messageS3).build();
    challengeService.saveInformation(SATO, request, IP_ADDRESS);
    TopSecretResponse topSecretResponse = challengeService.saveInformation(SATO, request, IP_ADDRESS);
    assertEquals(ConsUtil.MESSAGE_OK, topSecretResponse.getOkMessage());
  }

  /* getInformation */
  @Test
  void getInformation_success() throws MissingInformationException, PositionNotDeterminedException,
      MessageNotDeterminedException {

    // kenobi
    TopSecretSplitRequest request =
        TopSecretSplitRequest.builder().distance(DISTANCE_TO_KENOBI).message(messageS1).build();
    challengeService.saveInformation(KENOBI, request, IP_ADDRESS);

    // skywalker
    request = TopSecretSplitRequest.builder().distance(DISTANCE_TO_SKYWALKER).message(messageS2).build();
    challengeService.saveInformation(SKYWALKER, request, IP_ADDRESS);

    // sato
    request = TopSecretSplitRequest.builder().distance(DISTANCE_TO_SATO).message(messageS3).build();
    challengeService.saveInformation(SATO, request, IP_ADDRESS);

    TopSecretResponse response = challengeService.getInformation(IP_ADDRESS);
    Position positionExpected  =  Position.builder().x(-100).y(75.5f).build();
    assertEquals(positionExpected, response.getPosition());
    assertEquals("este es un mensaje", response.getMessage());
  }

  @Test
  void getInformation_missingSatellite() {

    // kenobi
    TopSecretSplitRequest request =
        TopSecretSplitRequest.builder().distance(DISTANCE_TO_KENOBI).message(messageS1).build();
    challengeService.saveInformation(KENOBI, request, IP_ADDRESS);

    // skywalker
    request = TopSecretSplitRequest.builder().distance(DISTANCE_TO_SKYWALKER).message(messageS2).build();
    challengeService.saveInformation(SKYWALKER, request, IP_ADDRESS);

    Exception exception = assertThrows(MissingInformationException.class, () -> {
      challengeService.getInformation(IP_ADDRESS);
    });
    String expectedMessage = ConsUtil.MISSING_INFORMATION;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }

  @Test
  void getInformation_missingSatellites() {

    Exception exception = assertThrows(MissingInformationException.class, () -> {
      challengeService.getInformation(IP_ADDRESS);
    });
    String expectedMessage = ConsUtil.MISSING_INFORMATION;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void getInformation_messageNotDeterminated() {

    // kenobi
    TopSecretSplitRequest request =
        TopSecretSplitRequest.builder().distance(DISTANCE_TO_KENOBI).message(messageS1).build();
    challengeService.saveInformation(KENOBI, request, IP_ADDRESS);

    // skywalker
    request = TopSecretSplitRequest.builder().distance(DISTANCE_TO_SKYWALKER).message(messageS2).build();
    challengeService.saveInformation(SKYWALKER, request, IP_ADDRESS);

    // sato
    messageS3 = new String[]{"este", "", "mensaje", ""};
    request = TopSecretSplitRequest.builder().distance(DISTANCE_TO_SATO).message(messageS3).build();
    challengeService.saveInformation(SATO, request, IP_ADDRESS);

    Exception exception = assertThrows(MessageNotDeterminedException.class, () -> {
      challengeService.getInformation(IP_ADDRESS);
    });
    String expectedMessage = ConsUtil.MESSAGE_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }

  /* getMessage */

  @Test
  void getMessageTest_messageDeterminated() throws MessageNotDeterminedException {
    messageS1 = new String[]{"este", "es", "un", "mensaje"};
    messageS2 = new String[]{"este", "", "un", "mensaje"};
    messageS3 = new String[]{"", "es", "", "mensaje"};
    final String actual = challengeService.getMessage(messageS1, messageS2, messageS3);
    assertEquals("este es un mensaje", actual);
  }

  @Test
  void getMessageTest_FailByDifferentSizes() {
    messageS1 = new String[]{"este", "es", "un", "mensaje"};
    messageS2 = new String[]{"este", "", "un", "mensaje"};
    messageS3 = new String[]{"", "es", "", "mensaje", ""};
    Exception exception = assertThrows(MessageNotDeterminedException.class, () -> {
      challengeService.getMessage(messageS1, messageS2, messageS3);
    });
    String expectedMessage = ConsUtil.MESSAGE_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void getMessageTest_FailByNoElements() {
    messageS1 = new String[]{};
    messageS2 = new String[]{};
    messageS3 = new String[]{};
    Exception exception = assertThrows(MessageNotDeterminedException.class, () -> {
      challengeService.getMessage(messageS1, messageS2, messageS3);
    });
    String expectedMessage = ConsUtil.MESSAGE_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}