package org.teknichrono.rest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.teknichrono.util.CsvConverter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
class TestSeasonEndpoint {


  private static final Logger LOGGER = Logger.getLogger(TestSeasonEndpoint.class);

  @Test
  public void listsAllSeasons() {
    given()
        .when().get("/season")
        .then()
        .statusCode(200)
        .body("$.size()", is(74),
            "[0].current", is(Boolean.TRUE),
            "[0].id", is("db8dc197-c7b2-4c1b-b3a4-6dc534c014ef"),
            "[0].year", is(2022),
            "[73].current", is(Boolean.FALSE),
            "[73].id", is("72de1fab-255c-4b9a-9d4b-79ce47ab61c5"),
            "[73].year", is(1949));
  }

  @Test
  public void listsAllSeasonsAsCsv() {
    String content = given()
        .when().get("/season/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(75);
    assertThat(lines.get(0)).contains("YEAR");
    assertThat(lines.get(0)).contains("ID");
    assertThat(lines.get(0)).contains("CURRENT");
    assertThat(lines).anyMatch(s -> s.chars().filter(c -> c == ',').count() == 2);
    assertThat(lines).noneMatch(s -> s.contains("null"));
  }

  @Test
  public void getSessionClassificationAsCsvFails() throws IOException {
    CsvConverter mock = Mockito.mock(CsvConverter.class);
    Mockito.when(mock.convertToCsv(Mockito.anyList())).thenThrow(new IOException("Nope"));
    QuarkusMock.installMockForType(mock, CsvConverter.class);
    given()
        .when().get("/season/csv")
        .then()
        .statusCode(500);
  }

  @Test
  public void listsAllTests() {
    given()
        .when().get("/season/test")
        .then()
        .statusCode(200)
        .body("$.size()", is(8),
            "[0].current", is(Boolean.TRUE),
            "[0].id", is("db8dc197-c7b2-4c1b-b3a4-6dc534c014ef"),
            "[0].year", is(2022),
            "[7].current", is(Boolean.FALSE),
            "[7].year", is(2015));
  }

  @Test
  public void getCurrentSeason() {
    given()
        .when().get("/season/current")
        .then()
        .statusCode(200)
        .body("current", is(Boolean.TRUE),
            "id", is("db8dc197-c7b2-4c1b-b3a4-6dc534c014ef"),
            "year", is(2022));
  }

  @Test
  public void getCurrentTest() {
    given()
        .when().get("/season/current/test")
        .then()
        .statusCode(200)
        .body("current", is(Boolean.TRUE),
            "id", is("db8dc197-c7b2-4c1b-b3a4-6dc534c014ef"),
            "year", is(2022));
  }

}