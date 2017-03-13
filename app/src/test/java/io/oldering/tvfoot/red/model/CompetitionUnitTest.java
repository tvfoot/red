package io.oldering.tvfoot.red.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompetitionUnitTest {
  @Test public void create() {
    String code = "code";
    String name = "name";
    String country = "country";
    String url = "url";
    String gender = "gender";

    Competition competition = Competition.create(code, name, country, url, gender);

    assertEquals(competition.getCode(), code);
    assertEquals(competition.getName(), name);
    assertEquals(competition.getCountry(), country);
    assertEquals(competition.getUrl(), url);
    assertEquals(competition.getGender(), gender);
  }
}
