package io.oldering.tvfoot.red;

import org.junit.Test;

import io.oldering.tvfoot.red.model.Team;

import static org.junit.Assert.assertEquals;

public class TeamUnitTest {
    @Test
    public void create() {
        String code = "code";
        String name = "name";
        String fullname = "fullname";
        String city = "city";
        String country = "country";
        String url = "url";
        String stadium = "stadium";
        String twitter = "twitter";
        String type = "type";
        Team team = Team.create(code, name, fullname, city, country, url, stadium, twitter, type);

        assertEquals(team.getCode(), code);
        assertEquals(team.getName(), name);
        assertEquals(team.getFullname(), fullname);
        assertEquals(team.getCity(), city);
        assertEquals(team.getCountry(), country);
        assertEquals(team.getUrl(), url);
        assertEquals(team.getStadium(), stadium);
        assertEquals(team.getTwitter(), twitter);
        assertEquals(team.getType(), type);
    }
}
