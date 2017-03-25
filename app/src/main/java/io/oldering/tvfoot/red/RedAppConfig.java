package io.oldering.tvfoot.red;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class RedAppConfig {
  public static final List<String> SCHEMES =
      Collections.unmodifiableList(Arrays.asList("http", "https", "tvfoot"));
  public static final List<String> AUTHORITIES =
      Collections.unmodifiableList(Arrays.asList("tvfoot", "tvfoot.net"));
  public static final String PATH_MATCH = "match";
}
