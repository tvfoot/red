package io.oldering.tvfoot.red.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BroadcasterUnitTest {
  @Test public void create() {
    String name = "名前";
    String code = "コード";
    String url = "url";
    Broadcaster broadcaster = Broadcaster.create(name, code, url);

    assertEquals(broadcaster.getName(), name);
    assertEquals(broadcaster.getCode(), code);
    assertEquals(broadcaster.getUrl(), url);
  }
}
