package com.merdadolibre.challenge.utils;

import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrilateralationUtilTest {

  private static final double TOLERANCE_ERROR = 0.1;
  private Vector vector1;
  private Vector vector2;
  private Vector vector3;

  private float[] distances;

  @BeforeEach
  void setUp() {
    vector1 = Vector.builder().positionx(5.0).positiony(4.0).build();
    vector2 = Vector.builder().positionx(4.0).positiony(-3.0).build();
    vector3 = Vector.builder().positionx(-4.0).positiony(13.0).build();
    distances = new float[]{5, 5, 13};
  }

  /* determinePosition **/

  @Test
  void determinePositionTest_positionFound() throws PositionNotDeterminedException {
    Vector vectorExpected = Vector.builder().positionx(1).positiony(1).build();
    Vector vectorResult = TrilateralationUtil.determinePosition(vector1, vector2, vector3, distances, TOLERANCE_ERROR);
    assertEquals(vectorExpected, vectorResult);
  }

  @Test
  void determinePositionTest_NoPosition() {
    distances = new float[]{0,0,0};
    Exception exception = assertThrows(PositionNotDeterminedException.class, () -> {
      TrilateralationUtil.determinePosition(vector1, vector2, vector3, distances, TOLERANCE_ERROR);
    });
    String expectedMessage = ConsUtil.POSITION_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }


  @Test
  void determinePositionTest_positionFoundWithRealNumbers() throws PositionNotDeterminedException {
    Vector vectorExpected = Vector.builder().positionx(-100).positiony(75.5).build();
    vector1 = Vector.builder().positionx(-500).positiony(-200).build();
    vector2 = Vector.builder().positionx(100).positiony(-100).build();
    vector3 = Vector.builder().positionx(500).positiony(100).build();
    distances = new float[]{485.69f, 266.08f, 600.5f};
    Vector vectorResult = TrilateralationUtil.determinePosition(vector1, vector2, vector3, distances, TOLERANCE_ERROR);
    assertEquals(vectorExpected, vectorResult);
  }

  @Test
  void determinePositionTest_NoPosition_noSastifaceThirdEquation() {
    vector1 = Vector.builder().positionx(-500).positiony(-200).build();
    vector2 = Vector.builder().positionx(100).positiony(-100).build();
    vector3 = Vector.builder().positionx(500).positiony(100).build();
    distances = new float[]{485.69f, 266.08f, 0};
    Exception exception = assertThrows(PositionNotDeterminedException.class, () -> {
      distances[2] = 0;
      TrilateralationUtil.determinePosition(vector1, vector2, vector3, distances, TOLERANCE_ERROR);
    });

    String expectedMessage = ConsUtil.POSITION_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  /* checkSystemEquations **/

  @Test
  void checkSystemEquationsTest_sastifaceSystem() {
    final double x = 1;
    final double y = 1;
    boolean result =
        TrilateralationUtil.checkSystemEquations(vector1, vector2, vector3, distances, x, y, TOLERANCE_ERROR);
    assertTrue(result);
  }

  @Test
  void checkSystemEquationsTest_notSastifaceSystem() {
    final double x = 8;
    final double y = 0;
    boolean result =
        TrilateralationUtil.checkSystemEquations(vector1, vector2, vector3, distances, x, y, TOLERANCE_ERROR);
    assertFalse(result);
  }


  @Test
  void checkSystemEquationsTest_sastifaceSystemWithRealNumbers() {
    double x = -100;
    double y = 75.5;
    vector3 = Vector.builder().positionx(500).positiony(100).build();
    vector2 = Vector.builder().positionx(100).positiony(-100).build();
    vector1 = Vector.builder().positionx(-500).positiony(-200).build();
    distances = new float[]{485.69f, 266.08f, 600.5f};
    final boolean result =
        TrilateralationUtil.checkSystemEquations(vector1, vector2, vector3, distances, x, y, TOLERANCE_ERROR);
    assertTrue(result);
  }

  @Test
  void checkSystemEquationsTest_noSastifaceFirstEcuation() {
    double x = -100;
    double y = 75.5;
    vector3 = Vector.builder().positionx(500).positiony(100).build();
    vector2 = Vector.builder().positionx(100).positiony(-100).build();
    vector1 = Vector.builder().positionx(-500).positiony(-200).build();
    distances = new float[]{285.69f, 266.08f, 600.5f};
    final boolean result =
        TrilateralationUtil.checkSystemEquations(vector1, vector2, vector3, distances, x, y, TOLERANCE_ERROR);
    assertFalse(result);
  }

  @Test
  void checkSystemEquationsTest_noSastifaceSecondEcuation() {
    double x = -100;
    double y = 75.5;
    vector3 = Vector.builder().positionx(500).positiony(100).build();
    vector2 = Vector.builder().positionx(100).positiony(-100).build();
    vector1 = Vector.builder().positionx(-500).positiony(-200).build();
    distances = new float[]{485.69f, 366.08f, 600.5f};
    final boolean result =
        TrilateralationUtil.checkSystemEquations(vector1, vector2, vector3, distances, x, y, TOLERANCE_ERROR);
    assertFalse(result);
  }

  @Test
  void checkSystemEquationsTest_noSastifaceThirdEcuation() {
    double x = -100;
    double y = 75.5;
    vector3 = Vector.builder().positionx(500).positiony(100).build();
    vector2 = Vector.builder().positionx(100).positiony(-100).build();
    vector1 = Vector.builder().positionx(-500).positiony(-200).build();
    distances = new float[]{485.69f, 266.08f, 700.5f};
    final boolean result =
        TrilateralationUtil.checkSystemEquations(vector1, vector2, vector3, distances, x, y, TOLERANCE_ERROR);
    assertFalse(result);
  }

  /* check equalities ****/

  @Test
  void checkEqualitiesTest_sastifaceEquation() {
    final double x = 1;
    final double y = 1;
    vector1 = Vector.builder().positionx(5.0).positiony(4.0).build();
    boolean result = TrilateralationUtil.checkEqualities(vector1, distances[0], x, y, TOLERANCE_ERROR);
    assertTrue(result);
  }

  @Test
  void checkEqualitiesTest_notSastifaceEquation() {
    final double x = 8;
    final double y = 0;
    vector3 = Vector.builder().positionx(-4.0).positiony(13.0).build();
    boolean result = TrilateralationUtil.checkEqualities(vector3, distances[2], x, y, TOLERANCE_ERROR);
    assertFalse(result);
  }

  /* check quadratic ****/

  @Test
  void quadraticTest_oneRootFounded() throws PositionNotDeterminedException {
    double [] rootsExpected = TrilateralationUtil.quadratic(1, 2, 1);
    assertEquals(-1.0, rootsExpected[0]);
    assertEquals(1, rootsExpected.length);
  }

  @Test
  void quadraticTest_twoRootFounded() throws PositionNotDeterminedException {
    double [] rootsExpected = TrilateralationUtil.quadratic(-1, 4, -3);
    assertTrue(1.0 == rootsExpected[0] && 3.0 == rootsExpected[1]);
    assertEquals(2, rootsExpected.length);
  }

  @Test
  void quadraticTest_determinateNegative() {
    Exception exception = assertThrows(PositionNotDeterminedException.class, () -> {
      TrilateralationUtil.quadratic(1, 3, 5);
    });
    String expectedMessage = ConsUtil.POSITION_NOT_DETERMINED;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  /* roundOneDecimals */
  @Test
  void roundOneDecimalsTest() {
    distances = new float[]{485.69f, 266.08f, 600.5f};
    assertEquals(75.5,  TrilateralationUtil.roundOneDecimals(75.45));
    assertEquals(485.7,  TrilateralationUtil.roundOneDecimals(485.6978));
    assertEquals(266.1,  TrilateralationUtil.roundOneDecimals(266.083163691f));
    assertEquals(485.7,  TrilateralationUtil.roundOneDecimals(485.6956351461273f));
  }
}