package com.merdadolibre.challenge.service.challenge;

import com.merdadolibre.challenge.BaseTest;
import com.merdadolibre.challenge.exception.MessageNotDeterminedException;
import com.merdadolibre.challenge.utils.ConsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class ChallengeServiceTest extends BaseTest {

  @Autowired
  private ChallengeService challengeService;

  private String[] messageS1;
  private String[] messageS2;
  private String[] messageS3;

  public void septup() {
  }

  @Test
  void getMessage() throws MessageNotDeterminedException {
    messageS1 = new String[]{"este", "es", "un", "mensaje"};
    messageS2 = new String[]{"este", "", "un", "mensaje"};
    messageS3 = new String[]{"", "es", "", "mensaje"};
    final String actual = challengeService.getMessage(messageS1, messageS2, messageS3);
    assertEquals("este es un mensaje", actual);
  }

  @Test
  void getMessageFailByDifferentSizes() {
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
  void getMessageFailByNoElements() {
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