package io.oldering.tvfoot.red;

import org.junit.Test;

import io.oldering.tvfoot.red.model.Broadcaster;
import io.oldering.tvfoot.red.model.Competition;
import io.oldering.tvfoot.red.model.Team;

import static org.junit.Assert.assertEquals;

public class ModelUnitTest {
    @Test
    public void Broadcaster_create() {
        String name = "名前";
        String code = "コード";
        String url = "url";
        Broadcaster broadcaster = Broadcaster.create(name, code, url);

        assertEquals(broadcaster.getName(), name);
        assertEquals(broadcaster.getCode(), code);
        assertEquals(broadcaster.getUrl(), url);
    }

    @Test
    public void Team_create() {
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

    @Test
    public void Competition_create() {
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
