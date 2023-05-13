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
class TestSessionAnalysisCSVEndpoint {


  @Test
  void getTestAnalysis() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2022/JE1/motogp/FP2/analysis")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData).allMatch(l -> l.length == 15);

      assertThat(allData).allMatch(l -> Integer.parseInt(l[0]) > 0);
      assertThat(allData).allMatch(l -> l[1] != null).allMatch(l -> !l[1].isEmpty());
      assertThat(allData).allMatch(l -> l[2].length() == 2);
      assertThat(allData).allMatch(l -> l[3] != null).allMatch(l -> !l[3].isEmpty());
      assertThat(allData).allMatch(l -> l[4] != null).allMatch(l -> !l[4].isEmpty());
      assertThat(allData).allMatch(l -> Integer.parseInt(l[5]) > 0);
      assertThat(allData).anyMatch(l -> l[7] != null && Float.valueOf(l[7]) > 0);
      assertThat(allData).allMatch(l -> Boolean.valueOf(l[14]) || l[6] != null);
      assertThat(allData).allMatch(l -> l[8] != null).allMatch(l -> l[8].isEmpty());
      assertThat(allData).allMatch(l -> l[9] != null).allMatch(l -> l[9].isEmpty());
      assertThat(allData).allMatch(l -> l[10] != null).allMatch(l -> l[10].isEmpty());
      assertThat(allData).allMatch(l -> l[11] != null).allMatch(l -> l[11].isEmpty());
      assertThat(allData).anyMatch(l -> Boolean.valueOf(l[12]));
      assertThat(allData).anyMatch(l -> Boolean.valueOf(l[13]));
    }

  }


  @Test
  void getSessionAnalysisFailsBecauseOfSession() {
    given()
        .when().get("/api/csv/2021/QAT/motogp/fp9/analysis")
        .then()
        .statusCode(404);
  }

  @Test
  void getSessionAnalysisFailsBecauseOfEvent() {
    given()
        .when().get("/api/csv/2021/NOP/motogp/fp3/analysis")
        .then()
        .statusCode(404);
  }

  @Test
  void throwsErrorIfCantParseTestAnalysisPdf() {
    given()
        .when().get("/api/csv/2022/JE1/motogp/fp1/analysis")
        .then()
        .statusCode(500);
  }


  @Test
  void getPracticeAnalysis() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2021/QAT/motogp/fp3/analysis")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData).hasSize(313);

      assertThat(allData).allMatch(l -> l.length == 15);

      assertIsGoodCsv(allData);

      assertThat(allData.get(0)[0]).isEqualTo("21");
      assertThat(allData.get(0)[1]).containsIgnoringCase("Morbidelli");
      assertThat(allData.get(0)[2]).isEqualTo("IT");
      assertThat(allData.get(0)[3]).containsIgnoringCase("Yamaha");
      assertThat(allData.get(0)[4]).containsIgnoringCase("Yamaha");
      assertThat(Integer.parseInt(allData.get(0)[5])).isEqualTo(1);
      assertThat(allData.get(0)[6]).isNotNull();
      assertThat(Float.valueOf(allData.get(0)[7])).isEqualTo(171.1f);
      assertThat(allData.get(0)[8]).isEqualTo("Slick-Hard");
      assertThat(allData.get(0)[9]).isEqualTo("Slick-Hard");
      assertThat(Integer.parseInt(allData.get(0)[10])).isZero();
      assertThat(Integer.parseInt(allData.get(0)[11])).isZero();
      assertThat(Boolean.valueOf(allData.get(0)[12])).isFalse();
      assertThat(Boolean.valueOf(allData.get(0)[13])).isFalse();
    }

  }

  private void assertIsGoodCsv(List<String[]> allData) {
    assertThat(allData).allMatch(l -> Integer.parseInt(l[0]) > 0);
    assertThat(allData).allMatch(l -> l[1] != null).allMatch(l -> !l[1].isEmpty());
    assertThat(allData).allMatch(l -> l[2].length() == 2);
    assertThat(allData).allMatch(l -> l[3] != null).allMatch(l -> !l[3].isEmpty());
    assertThat(allData).allMatch(l -> l[4] != null).allMatch(l -> !l[4].isEmpty());
    assertThat(allData).allMatch(l -> Integer.parseInt(l[5]) > 0);
    assertThat(allData).anyMatch(l -> l[7] != null && Float.valueOf(l[7]) > 0);
    assertThat(allData).allMatch(l -> Boolean.valueOf(l[14]) || l[6] != null);
    assertThat(allData).allMatch(l -> l[8] != null).allMatch(l -> !l[8].isEmpty());
    assertThat(allData).allMatch(l -> l[9] != null).allMatch(l -> !l[9].isEmpty());
    assertThat(allData).allMatch(l -> l[10] != null).allMatch(l -> !l[10].isEmpty());
    assertThat(allData).allMatch(l -> l[11] != null).allMatch(l -> !l[11].isEmpty());
    assertThat(allData).anyMatch(l -> !Boolean.valueOf(l[12]));
    assertThat(allData).anyMatch(l -> !Boolean.valueOf(l[13]));
  }


  @Test
  void getRaceAnalysis() throws IOException, CsvException {
    String csv = given()
        .when().get("/api/csv/2021/QAT/motogp/RAC/analysis")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();

    assertThat(csv).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(csv)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();

      assertThat(allData).hasSize(439);

      assertThat(allData).allMatch(l -> l.length == 15);

      assertIsGoodCsv(allData);

      assertThat(allData.get(0)[0]).isEqualTo("12");
      assertThat(allData.get(0)[1]).containsIgnoringCase("Maverick");
      assertThat(allData.get(0)[2]).isEqualTo("ES");
      assertThat(allData.get(0)[3]).containsIgnoringCase("Yamaha");
      assertThat(allData.get(0)[4]).containsIgnoringCase("Yamaha");
      assertThat(Integer.parseInt(allData.get(0)[5])).isEqualTo(1);
      assertThat(allData.get(0)[6]).isNotNull();
      assertThat(Float.valueOf(allData.get(0)[7])).isEqualTo(192.8f);
      assertThat(allData.get(0)[8]).isEqualTo("Slick-Soft");
      assertThat(allData.get(0)[9]).isEqualTo("Slick-Soft");
      assertThat(Integer.parseInt(allData.get(0)[10])).isZero();
      assertThat(Integer.parseInt(allData.get(0)[11])).isZero();
      assertThat(Boolean.valueOf(allData.get(0)[12])).isFalse();
      assertThat(Boolean.valueOf(allData.get(0)[13])).isFalse();
    }
  }

}