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
class TestRiderCSVEndpoint {


  @Test
  void listRidersOfCategoryOfEvent() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2021/QAT/GP/riders")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();
    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData.stream().allMatch(l -> l.length == 11)).isTrue();

      assertThat(allData).hasSize(23);
      assertThat(allData.stream().allMatch(rider -> rider[0] != null &&
          rider[1] != null &&
          rider[3] != null &&
          rider[4] != null)).isTrue();
    }
  }

  @Test
  void errorIfCategoryDoesNotExist() {
    given()
        .when().get("/api/csv/2021/QAT/MOTO9/riders")
        .then()
        .statusCode(404);
  }

  @Test
  void errorIfCantFindRidersOfCategory() {
    given()
        .when().get("/api/csv/2021/QAT/MOTO2/riders")
        .then()
        .statusCode(404);
  }


  @Test
  void errorIfCantFindYear() {
    given()
        .when().get("/api/csv/12021/QAT/GP/riders")
        .then()
        .statusCode(404);
  }
}