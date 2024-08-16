package org.teknichrono.mgp.csv.rest;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import io.quarkus.test.common.WithTestResource;
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
@WithTestResource(WireMockExtensions.class)
class TestCategoryCSVEndpoint {

  @Test
  void getOneParticularEvent() throws IOException, CsvException {
    String csv = given()
        .when().get(" /api/csv/2021/QAT")
        .then()
        .contentType("text/csv")
        .statusCode(200)
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData).hasSize(3);
      assertThat(allData.stream().allMatch(l -> l.length == 2)).isTrue();

      assertThat(allData.stream().filter(l -> l[0].contains("Moto3")).findFirst()).isPresent();
    }
  }

  @Test
  void getOneParticularTest() throws IOException, CsvException {
    String csv = given()
        .when().get(" /api/csv/2022/JE1")
        .then()
        .contentType("text/csv")
        .statusCode(200)
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData).hasSize(1);
      assertThat(allData.stream().allMatch(l -> l.length == 2)).isTrue();

      assertThat(allData.stream().filter(l -> l[0].contains("MotoGP")).findFirst()).isPresent();
    }
  }

  @Test
  void failToFindEvent() {
    given()
        .when().get(" /api/csv/2021/NOP")
        .then()
        .statusCode(404);
  }

  @Test
  void failToFindEventBecauseOfYear() {
    given()
        .when().get(" /api/csv/2299/NOP")
        .then()
        .statusCode(404);
  }
}