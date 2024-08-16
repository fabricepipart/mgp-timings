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
class TestSessionResultsCSVEndpoint {

  @Test
  void getSessionClassification() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2021/QAT/motogp/fp3")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();
      assertThat(allData).hasSize(22);

      assertThat(allData).allMatch(l -> l.length == 15);

      validateCsv(allData);

      assertThat(allData.get(0)[0]).isEqualTo("1");
      assertThat(allData.get(0)[1]).isEqualTo("21");
      assertThat(allData.get(0)[3]).containsIgnoringCase("Franco");
      assertThat(allData.get(0)[4]).isEqualTo("ITA");
      assertThat(allData.get(0)[5]).containsIgnoringCase("Yamaha");
      assertThat(allData.get(0)[6]).containsIgnoringCase("Yamaha");
      assertThat(allData.get(0)[10]).isEqualTo("17");
      assertThat(Float.valueOf(allData.get(0)[11])).isEqualTo(0f);
      assertThat(allData.get(0)[7]).isEqualTo("1'54.676");
      assertThat(allData.get(0)[8]).isEqualTo("14");
      assertThat(Float.valueOf(allData.get(0)[14])).isEqualTo(343.9f);

      assertThat(allData.get(1)[6]).containsIgnoringCase("Aprilia");
      assertThat(Float.valueOf(allData.get(1)[12])).isEqualTo(0.165f);
      assertThat(allData.get(1)[4]).isEqualTo("SPA");

    }
  }

  private void validateCsv(List<String[]> allData) {
    assertThat(allData).allMatch(l -> l[0].isEmpty() || Integer.parseInt(l[0]) > 0);
    assertThat(allData).allMatch(l -> Integer.parseInt(l[1]) > 0);
    assertThat(allData).allMatch(l -> l[2].isEmpty() || Integer.parseInt(l[2]) >= 0);
    assertThat(allData).allMatch(l -> l[3] != null).allMatch(l -> !l[3].isEmpty());
    assertThat(allData).allMatch(l -> l[4] != null).allMatch(l -> !l[4].isEmpty());
    assertThat(allData).allMatch(l -> l[5] != null).allMatch(l -> !l[5].isEmpty());
    assertThat(allData).allMatch(l -> l[6] != null).allMatch(l -> !l[6].isEmpty());
    assertThat(allData).allMatch(l -> l[8].isEmpty() || Integer.parseInt(l[8]) > 0);
    assertThat(allData).allMatch(l -> l[10].isEmpty() || Integer.parseInt(l[10]) >= 0);
    assertThat(allData).anyMatch(l -> l[11].isEmpty() || Float.valueOf(l[11]) >= 0);
    assertThat(allData).anyMatch(l -> l[12].isEmpty() || Float.valueOf(l[12]) >= 0);
    assertThat(allData).anyMatch(l -> l[14].isEmpty() || Float.valueOf(l[14]) > 0);
  }

  @Test
  void getSessionClassificationFailsBecauseOfSession() {
    given()
        .when().get("/api/csv/2021/QAT/motogp/fp9")
        .then()
        .statusCode(404);
  }

  @Test
  void getSessionClassificationFailsBecauseOfEvent() {
    given()
        .when().get("/api/csv/2021/NOP/motogp/fp3")
        .then()
        .statusCode(404);
  }

  @Test
  void getSessionClassificationFailsBecauseOfCategory() {
    given()
        .when().get("/api/csv/2021/QAT/MOTO9/fp3")
        .then()
        .statusCode(404);
  }

  @Test
  void getTestClassificationDetailsError() {
    given()
        .when().get("/api/csv/2022/MY1/GP/FP1")
        .then()
        .statusCode(500);
  }

  @Test
  void getTestSessionClassification() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2022/JE1/GP/FP2")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();
      assertThat(allData).hasSize(29);

      assertThat(allData).allMatch(l -> l.length == 15);

      validateCsv(allData);

      assertThat(allData.get(0)[0]).isEqualTo("1");
      assertThat(allData.get(0)[1]).isEqualTo("63");
      assertThat(allData.get(0)[3]).containsIgnoringCase("Francesco");
      assertThat(allData.get(0)[4]).isEqualTo("ITA");
      assertThat(allData.get(0)[5]).containsIgnoringCase("Ducati");
      assertThat(allData.get(0)[6]).containsIgnoringCase("Ducati");
      assertThat(allData.get(0)[7]).isEqualTo("1'36.872");
      assertThat(allData.get(0)[8]).isEqualTo("12");
      assertThat(allData.get(0)[10]).isEqualTo("42");
      assertThat(Float.valueOf(allData.get(0)[11])).isEqualTo(0f);
      assertThat(Float.valueOf(allData.get(0)[14])).isEqualTo(295.8f);

      assertThat(allData.get(1)[6]).containsIgnoringCase("Yamaha");
      assertThat(Float.valueOf(allData.get(1)[12])).isEqualTo(0.452f);
      assertThat(allData.get(1)[4]).isEqualTo("FRA");
    }

  }


  @Test
  void getRaceClassification() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2021/QAT/motogp/rac")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();
      assertThat(allData).hasSize(22);

      assertThat(allData).allMatch(l -> l.length == 15);

      validateCsv(allData);

      assertThat(allData.get(0)[0]).isEqualTo("1");
      assertThat(allData.get(0)[2]).isEqualTo("25");
      assertThat(allData.get(0)[9]).isEqualTo("42'28.663");
      assertThat(allData.get(0)[4]).isEqualTo("SPA");
      assertThat(Float.valueOf(allData.get(0)[13])).isEqualTo(167.1f);
      assertThat(allData.get(0)[10]).isEqualTo("22");


      assertThat(allData.get(1)[6]).containsIgnoringCase("Ducati");
      assertThat(allData.get(1)[3]).containsIgnoringCase("Zarco");
    }

  }
}