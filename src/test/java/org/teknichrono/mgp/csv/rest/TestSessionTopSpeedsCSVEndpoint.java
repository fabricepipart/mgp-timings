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
class TestSessionTopSpeedsCSVEndpoint {

  @Test
  void parsesMaxSpeedPdf() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2021/QAT/motogp/FP1/topspeed")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();


    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();
      assertThat(allData).hasSize(22);

      assertThat(allData).allMatch(l -> l.length == 6);

      assertThat(allData).allMatch(l -> Integer.parseInt(l[0]) > 0);
      assertThat(allData).allMatch(l -> l[1] != null).allMatch(l -> !l[1].isEmpty());
      assertThat(allData).allMatch(l -> l[2] != null).allMatch(l -> !l[2].isEmpty());
      assertThat(allData).allMatch(l -> l[3] != null).allMatch(l -> !l[3].isEmpty());
      assertThat(allData).allMatch(l -> l[4] != null).allMatch(l -> !l[4].isEmpty());
      assertThat(allData).anyMatch(l -> l[5] != null && Float.valueOf(l[5]) > 0);

      assertThat(Float.valueOf(allData.get(0)[5])).isEqualTo(354.0f);
      assertThat(allData.get(0)[4]).isEqualTo("Ducati");
      assertThat(allData.stream().filter(m -> m[0].equals("23")).findFirst().get()[3]).isEqualTo("Avintia Esponsorama");

    }
  }

  @Test
  void getSessionTopSpeedsFailsBecauseOfSession() {
    given()
        .when().get("/api/csv/2021/QAT/motogp/fp9/topspeed")
        .then()
        .statusCode(404);
  }

  @Test
  void getSessionTopSpeedsFailsBecauseOfEvent() {
    given()
        .when().get("/api/csv/2021/NOP/motogp/fp3/topspeed")
        .then()
        .statusCode(404);
  }

  @Test
  void throwsErrorIfCantParsePdf() {
    given()
        .when().get("/api/csv/2021/QAT/motogp/FP2/topspeed")
        .then()
        .statusCode(500);
  }

  @Test
  void throwsErrorIfNoPdfForTopSpeed() {
    given()
        .when().get("/api/csv/2021/QAT/motogp/FP3/topspeed")
        .then()
        .statusCode(404);
  }

}