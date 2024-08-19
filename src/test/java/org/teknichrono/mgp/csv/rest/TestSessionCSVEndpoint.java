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
class TestSessionCSVEndpoint {


  @Test
  void listsAllSessionsOfCategoryOfEvent() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2021/QAT/motogp")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();


    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData).hasSize(8);
      assertThat(allData).allMatch(l -> l.length == 5);
      assertThat(allData.get(0)[0]).isEqualTo("FP1");

    }
  }

  @Test
  void errorIfCategoryDoesNotExist() {
    given()
        .when().get("/api/csv/2021/QAT/MOTO9")
        .then()
        .statusCode(404);
  }

  @Test
  void errorIfYearDoesNotExist() {
    given()
        .when().get("/api/csv/12021/QAT/MOTOGP")
        .then()
        .statusCode(404);
  }

  @Test
  void listsAllSessionsOfCategoryOfTest() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2022/JE1/GP")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();


    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData).hasSize(2);
      assertThat(allData).allMatch(l -> l.length == 5);
      assertThat(allData.get(0)[0]).isEqualTo("FP1");

    }
  }

}