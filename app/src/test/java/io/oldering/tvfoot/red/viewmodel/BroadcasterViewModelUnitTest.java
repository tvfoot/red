package io.oldering.tvfoot.red.viewmodel;

import io.oldering.tvfoot.red.model.Broadcaster;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BroadcasterViewModelUnitTest {
  @Test public void create() {
    String name = "名前";
    String code = "コード";
    String url = "url";
    Broadcaster broadcaster = Broadcaster.create(name, code, url);

    BroadcasterViewModel broadcasterViewModel = BroadcasterViewModel.create(broadcaster);

    assertEquals(broadcasterViewModel.getCode(), code);
  }
}
