package com.merdadolibre.challenge.utils;

import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrilateralationUtilTest {

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

  @Test
  void determinePositionFounded() throws PositionNotDeterminedException {
    Vector vectorExpected = Vector.builder().positionx(1).positiony(1).build();
    Vector vectorResult = TrilateralationUtil.determinePosition(vector1, vector2, vector3, distances);
    assertEquals(vectorExpected, vectorResult);
  }

  @Test
  void determinePositionNoFounded() throws PositionNotDeterminedException {
    distances = new float[]{0,0,0};
    Vector vectorResult = TrilateralationUtil.determinePosition(vector1, vector2, vector3, distances);
    assertNull(vectorResult);
  }

  @Test
  void checkSystemEquationsTrue() {
    final double x = 1;
    final double y = 1;
    boolean result = TrilateralationUtil.checkSystemEquations(vector1, vector2, vector3, distances, x, y);
    assertTrue(result);
  }

  @Test
  void checkSystemEquationsFalse() {
    final double x = 8;
    final double y = 0;
    boolean result = TrilateralationUtil.checkSystemEquations(vector1, vector2, vector3, distances, x, y);
    assertFalse(result);
  }

  @Test
  void checkEqualitiesTrue() {
    final double x = 1;
    final double y = 1;
    boolean result = TrilateralationUtil.checkEqualities(vector1, distances[0], x, y);
    assertTrue(result);
  }

  @Test
  void checkEqualitiesFalse() {
    final double x = 8;
    final double y = 0;
    boolean result = TrilateralationUtil.checkEqualities(vector3, distances[2], x, y);
    assertFalse(result);
  }
}