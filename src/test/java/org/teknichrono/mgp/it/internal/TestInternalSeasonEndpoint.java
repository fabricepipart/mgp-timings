package org.teknichrono.mgp.it.internal;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.csv.converter.CsvConverterFactory;
import org.teknichrono.mgp.csv.converter.CsvConverterInterface;
import org.teknichrono.mgp.it.WireMockExtensions;

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
class TestInternalSeasonEndpoint {

  @InjectSpy
  CsvConverterFactory csvFactory;

  @Test
  void listsAllSeasons() {
    given()
        .when().get(" /api/internal/season")
        .then()
        .statusCode(200)
        .body("$.size()", is(75),
            "[0].current", is(Boolean.TRUE),
            "[0].id", is("db8dc197-c7b2-4c1b-b3a4-6dc534c023ef"),
            "[0].year", is(2023),
            "[73].current", is(Boolean.FALSE),
            "[74].id", is("72de1fab-255c-4b9a-9d4b-79ce47ab61c5"),
            "[74].year", is(1949));
  }

  @Test
  void listsAllSeasonsAsCsv() {
    String content = given()
        .when().get(" /api/internal/season/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines).hasSize(76);
    assertThat(lines.get(0))
        .contains("YEAR")
        .contains("CURRENT");
    assertThat(lines)
        .anyMatch(s -> s.chars().filter(c -> c == ',').count() == 1)
        .noneMatch(s -> s.contains("null"));
  }


  @Test
  void listsAllSeasonsAsCsvFails() throws IOException {
    CsvConverterInterface seasonCsvConverter = Mockito.mock(CsvConverterInterface.class);
    Mockito.when(csvFactory.getCsvConverter(Season.class)).thenReturn(seasonCsvConverter);
    Mockito.when(seasonCsvConverter.convertToCsv(Mockito.anyList())).thenThrow(new IOException("Nope"));
    given()
        .when().get("/api/internal/season/csv")
        .then()
        .statusCode(500);
  }

  @Test
  void getCurrentSeason() {
    given()
        .when().get(" /api/internal/season/current")
        .then()
        .statusCode(200)
        .body("current", is(Boolean.TRUE),
            "id", is("db8dc197-c7b2-4c1b-b3a4-6dc534c023ef"),
            "year", is(2023));
  }

}