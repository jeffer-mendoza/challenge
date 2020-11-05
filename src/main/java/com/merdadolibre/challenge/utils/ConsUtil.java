package com.merdadolibre.challenge.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Util Constants.
 *
 * @author Jefferson Mendoza, jefferson.mendoza@fonyou.com
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConsUtil {

  public static final String BEGIN_METHOD = "inside method";
  public static final String END_METHOD = "end method";

  // exception message
  public static final String POSITION_NOT_DETERMINED = "position not determined";
  public static final String DISTANCE_NOT_DETERMINED = "information was not sent";
  public static final String MESSAGE_NOT_DETERMINED = "missing information";
  public static final String POSITION_IS_NULL = "ship position is null";

}
