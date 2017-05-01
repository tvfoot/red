package io.oldering.tvfoot.red.app.common;

public class TimeConstants {
  private static final long ONE_SECOND_IN_MILLIS = 1000;
  public static final long ONE_MINUTE_IN_MILLIS = 60 * ONE_SECOND_IN_MILLIS;
  public static final long ONE_MATCH_TIME_IN_MILLIS = 105 * ONE_MINUTE_IN_MILLIS;
  private static final long ONE_HOUR_IN_MILLIS = 60 * ONE_MINUTE_IN_MILLIS;
  public static final long ONE_DAY_IN_MILLIS = 24 * ONE_HOUR_IN_MILLIS;

  private TimeConstants() {
    throw new RuntimeException("Can't touch this");
  }
}
