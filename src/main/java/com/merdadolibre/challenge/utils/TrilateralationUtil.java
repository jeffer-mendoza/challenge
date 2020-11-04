package com.merdadolibre.challenge.utils;

import com.merdadolibre.challenge.exception.PositionNotDeterminedException;
import com.merdadolibre.challenge.model.Vector;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class that implements the mathematical method of trilateration to find a relative point in the plane.
 *
 * @see <a href="https://es.wikipedia.org/wiki/Trilateraci%C3%B3n">Article describing the mathematical method</a>
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class TrilateralationUtil {

  /**
   * Determine the position of a point using the mathematical method of trilateration to determine the relative <b/>
   * positions of objects using triangle geometry analogous to triangulation.
   *
   * <p>
   *   The system of equations is composed of the equality given by the equation of the distance between two points.
   *   given the points:
   *   <b/>
   *   P1 = (a,b)<b/>
   *   P2 = (c,d)<b/>
   *   P3 = (e,f)<b/>
   *   and the point to find (x,y)<b/>
   *   <b/>
   *   (x - a)^2 + (y - b)^2 = r1^2
   *   (x - a)^2 + (y - b)^2 = r2^2
   *   (x - a)^2 + (y - b)^2 = r3^2
   * </p>
   * <p>
   *   if the ordered pair satisfies the entire system of equations, the point is determined,
   *   otherwise null is returned.
   * </p>
   *
   * @param vector1 satellite position1
   * @param vector2 satellite position2
   * @param vector3 satellite position3
   * @param distances distance between the satellites and the point to find,
   *                 each position must correspond to the points of the satellites
   * @return the vector composed of the ordered pair found, otherwise null
   */
  public static Vector determinePosition(final Vector vector1, final Vector vector2, final Vector vector3,
                                         final float[]distances) throws PositionNotDeterminedException {
    try {
      log.debug("vector1: {} - vector2: {} - vector3: {} - distances: {}", vector1, vector2, vector3, distances);
      // substitutions used in the mathematical model
      final double sutitutionK = (Math.pow(distances[0], 2) - Math.pow(vector1.getPositionx(), 2)
          - Math.pow(vector1.getPositiony(), 2) + +Math.pow(vector2.getPositionx(), 2)
          + Math.pow(vector2.getPositiony(), 2) - Math.pow(distances[1], 2))
          / (2 * (vector2.getPositionx() - vector1.getPositionx()));

      final double sustitutionI = (vector1.getPositiony() - vector2.getPositiony())
          / (vector2.getPositionx() - vector1.getPositionx());

      log.debug("sustitutionK: {} - sustitutionI: {}", sutitutionK, sustitutionI);

      // find roots of y, they will be two because the equation is a quadratic
      final double y1 = Math.pow(distances[0], 2) - Math.pow(vector1.getPositionx(), 2)
          - Math.pow(vector1.getPositiony(), 2) - Math.pow(sutitutionK, 2) + 2 * vector1.getPositionx() * sutitutionK;
      final double y2 = (y1 - (2 * sustitutionI * sutitutionK) + (2 * vector1.getPositionx() * sustitutionI)
          + (2 * vector1.getPositiony())) / (Math.pow(sustitutionI, 2) + 1);

      log.debug("root founded: y1:{} - y2:{}", y1, y2);

      // find the roots of x according to the roots of y
      final double x1 = y1 * sustitutionI + sutitutionK;
      final double x2 = y2 * sustitutionI + sutitutionK;

      log.debug("root founded: x1:{} - x2:{}", y1, y2);

      if (checkSystemEquations(vector1, vector2, vector3, distances, x1, y1)) {
        return Vector.builder().positionx(x1).positiony(y1).build();
      } else if (checkSystemEquations(vector1, vector2, vector3, distances, x2, y2)) {
        return Vector.builder().positionx(x2).positiony(y2).build();
      } else {
        return null;
      }
    } catch (Exception ex) {
      log.error("error", ex);
      throw new PositionNotDeterminedException(ConsUtil.POSITION_NOT_DETERMINED);
    }
  }

  /**
   * check the system of equations.
   *
   * @param vector1 satellite position1
   * @param vector2 satellite position2
   * @param vector3 satellite position3
   * @param distances distance between the satellites and the point to find,
   *                 each position must correspond to the points of the satellites
   * @param rootX root of x found
   * @param rootY root of y found
   * @return true if it satisfies the system of equations, otherwise false
   */
  public static boolean checkSystemEquations(final Vector vector1, final Vector vector2, final Vector vector3,
                                        final float[]distances, final double rootX, final double rootY) {
    return checkEqualities(vector1, distances[0], rootX, rootY)
        && checkEqualities(vector2, distances[1], rootX, rootY)
        && checkEqualities(vector3, distances[2], rootX, rootY);
  }

  /**
   * check the equality of an equation.
   *
   * @param vector point on the plane
   * @param distance distance between the given vector and the ordered pair composed of x and y
   * @param rootX root of x found
   * @param rootY root of y found
   * @return true if it satisfies equality, otherwise false
   */
  public static boolean checkEqualities(final Vector vector, final float distance, final double rootX,
                                        final double rootY) {
    final double leftCalculate = Math.pow(rootX - vector.getPositionx(), 2) + Math.pow(rootY - vector.getPositiony(), 2);
    final double rigthCalculate = Math.pow(distance, 2);
    log.debug("equation {} == {}", leftCalculate, rigthCalculate);
    return leftCalculate == rigthCalculate;
  }

}
