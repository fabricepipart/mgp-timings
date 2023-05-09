package org.teknichrono.mgp.csv.rest;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.it.WireMockExtensions;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
class TestEventCSVEndpoint {


  @Test
  void listsAllEvents() throws IOException, CsvException {
    String csv = given()
        .when().get(" /api/csv/2021")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData.stream().allMatch(l -> l.length == 8)).isTrue();

      assertThat(allData).hasSize(24);
      assertThat(allData.stream().filter(l -> "QAT".equalsIgnoreCase(l[2])).findFirst()).isPresent();
      assertThat(allData.stream().filter(l -> "QAT".equalsIgnoreCase(l[2])).findFirst().get()).contains("GRAND PRIX OF QATAR", atIndex(0));
      assertThat(allData.stream().filter(l -> "QAT".equalsIgnoreCase(l[2])).findFirst().get()).contains("false", atIndex(3));
      assertThat(allData.stream().filter(l -> "MI1".equalsIgnoreCase(l[2])).findFirst()).isPresent();
      assertThat(allData.stream().filter(l -> "MI1".equalsIgnoreCase(l[2])).findFirst().get()).contains("2021-09-21", atIndex(4));
      assertThat(allData.stream().filter(l -> "MI1".equalsIgnoreCase(l[2])).findFirst().get()).contains("true", atIndex(3));

    }
  }


  @Test
  void listsAllEventsOfSeasonThatDoesNotExist() {
    given().when().get(" /api/csv/12021")
        .then()
        .statusCode(404);
  }
}