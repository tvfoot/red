package io.oldering.tvfoot.red.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.oldering.tvfoot.red.data.entity.Match;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class Fixture {
  Gson gson;

  @Inject public Fixture(Gson gson) {
    this.gson = gson;
  }

  public List<Match> readJsonStream(InputStream in) throws IOException {
    JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    List<Match> messages = new ArrayList<>();
    reader.beginArray();
    while (reader.hasNext()) {
      Match message = gson.fromJson(reader, Match.class);
      messages.add(message);
    }
    reader.endArray();
    reader.close();
    return messages;
  }

  public String readInputStream(InputStream is) {
    BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    StringBuilder total;
    try {
      total = new StringBuilder(is.available());
      String line;
      while ((line = r.readLine()) != null) {
        total.append(line);
      }
      return total.toString();
    } catch (IOException e) {
      Timber.e(e);
      return null;
    }
  }
}
