package org.teknichrono.mgp.it;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.teknichrono.mgp.model.out.PracticeClassificationDetails;
import org.teknichrono.mgp.model.out.RaceClassificationDetails;
import org.teknichrono.mgp.model.result.SessionClassification;
import org.teknichrono.mgp.util.CsvConverter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.is;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestSessionEndpoint {


  @Test
  public void listsAllSessionsOfCategoryOfEvent() {
    given()
        .when().get("/session/2021/QAT/motogp")
        .then()
        .statusCode(200)
        .body("$.size()", is(8),
            "[0].id", is("117144ae-2a0b-4d42-8d89-ab96253470d2"),
            "[0].type", containsStringIgnoringCase("FP"),
            "[0].number", is(1),
            "[0].circuit", containsStringIgnoringCase("Lusail"));
  }

  @Test
  public void parsesMaxSpeedPdf() {
    List<Map> speeds = given()
        .when().get("/session/2021/QAT/motogp/FP1/topspeed")
        .then()
        .statusCode(200).extract().as(List.class);

    assertThat(speeds.size()).isEqualTo(22);
    assertThat(speeds.get(0).get("speed")).isEqualTo(354.0);
    assertThat((String) speeds.get(0).get("motorcycle")).isEqualToIgnoringCase("Ducati");
    assertThat(speeds.size()).isEqualTo(22);
    assertThat(speeds.stream().filter(m -> ((Integer) m.get("number")).intValue() == 23).findFirst().get().get("team")).isEqualTo("Avintia Esponsorama");

    assertThat(speeds).anyMatch(s -> s.get("number") != null &&
        s.get("rider") != null &&
        s.get("nation") != null &&
        s.get("team") != null &&
        s.get("motorcycle") != null &&
        s.get("speed") != null);
  }

  @Test
  public void throwsErrorIfCantParsePdf() {
    given()
        .when().get("/session/2021/QAT/motogp/FP2/topspeed")
        .then()
        .statusCode(500);
  }

  @Test
  public void throwsErrorIfNoPdf() {
    given()
        .when().get("/session/2021/QAT/motogp/FP3/topspeed")
        .then()
        .statusCode(404);
  }

  @Test
  public void getSessionClassification() {
    SessionClassification classification = given()
        .when().get("/session/2021/QAT/motogp/fp3/results")
        .then()
        .statusCode(200).extract().as(SessionClassification.class);
    assertThat(classification.classification.size()).isEqualTo(22);
    assertThat(classification.classification.get(0).rider.full_name).containsIgnoringCase("Franco");
    assertThat(classification.classification.get(0).total_laps).isEqualTo(17);
    assertThat(classification.file).isNotNull();
    assertThat(classification.records.stream().filter(r -> r.type.equalsIgnoreCase("bestLap")).findFirst().get().rider.full_name).containsIgnoringCase("Marquez");
  }


  @Test
  public void getRaceClassification() {
    SessionClassification classification = given()
        .when().get("/session/2021/QAT/motogp/rac/results")
        .then()
        .statusCode(200).extract().as(SessionClassification.class);
    assertThat(classification.classification.size()).isEqualTo(22);
    assertThat(classification.classification.get(0).position).isEqualTo(1);
    assertThat(classification.classification.get(0).total_laps).isEqualTo(22);
    assertThat(classification.file).containsIgnoringCase("Classification.pdf");
    assertThat(classification.records.stream().filter(r -> r.type.equalsIgnoreCase("poleLap")).findFirst().get().rider.full_name).containsIgnoringCase("Bagnaia");
  }


  @Test
  public void getSessionClassificationAsCsv() {
    String content = given()
        .when().get("/session/2021/QAT/motogp/fp3/results/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(23);
    assertThat(lines.get(0)).contains("BEST_LAP");
    assertThat(lines.get(0)).contains("RIDER");
    assertThat(lines.get(0)).contains("TOTAL_LAPS");
    assertThat(lines).anyMatch(s -> s.chars().filter(c -> c == ',').count() == 7);
    assertThat(lines).noneMatch(s -> s.contains("null"));
  }

  @Test
  public void getSessionClassificationAsCsvFails() throws IOException {
    CsvConverter mock = Mockito.mock(CsvConverter.class);
    Mockito.when(mock.convertToCsv(Mockito.anyList(), Mockito.any())).thenThrow(new IOException("Nope"));
    QuarkusMock.installMockForType(mock, CsvConverter.class);
    given()
        .when().get("/session/2021/QAT/motogp/fp3/results/csv")
        .then()
        .statusCode(500);
  }


  @Test
  public void getRaceClassificationDetails() {
    List<RaceClassificationDetails> details = given()
        .when().get("/session/2021/QAT/motogp/rac/results/details")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<RaceClassificationDetails>>() {
        });

    assertThat(details.size()).isEqualTo(22);
    assertThat(details.get(0).averageSpeed).isEqualTo(167.1f);
    assertThat(details.get(0).position).isEqualTo(1);
    assertThat(details.get(0).points).isEqualTo(25);
    assertThat(details.get(0).nation).isEqualTo("SPA");
    assertThat(details.get(0).totalLaps).isEqualTo(22);
    assertThat(details.get(0).totalTime).isEqualTo("42'28.663");

    assertThat(details.get(1).constructor).containsIgnoringCase("Ducati");
    assertThat(details.get(1).riderName).containsIgnoringCase("Zarco");

    Float currentGapToFirst = 0f;
    Integer currentPosition = 0;
    Integer currentPoints = 0;
    for (RaceClassificationDetails d : details) {
      assertThat(d.gapToFirst == null || d.gapToFirst.floatValue() >= currentGapToFirst);
      if (currentGapToFirst == null) {
        assertThat(d.gapToFirst).isNull();
      } else {
        currentGapToFirst = d.gapToFirst;
      }
      assertThat(d.points == null || d.points.intValue() < currentPoints);
      if (currentPoints == null) {
        assertThat(d.points).isNull();
      } else {
        currentPoints = d.points;
      }
    }

    Assertions.assertTrue(details.stream().anyMatch(d -> d.position > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.riderNumber > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.riderName != null));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.nation.length() == 3));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.team != null));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.constructor != null));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.totalLaps > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.averageSpeed > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.totalTime != null));
  }

  @Test
  public void getRaceClassificationDetailsAsCsv() {
    String content = given()
        .when().get("/session/2021/QAT/motogp/rac/results/details/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(23);
    assertThat(lines.get(0)).containsAnyOf("POSITION", "POINTS", "NUMBER", "NAME", "NATION", "TEAM", "CONSTRUCTOR", "TOTAL_TIME", "TOTAL_LAPS", "GAP_TO_FIRST", "AVERAGE_SPEED");
    assertThat(lines).anyMatch(s -> s.chars().filter(c -> c == ',').count() == 10);
  }


  @Test
  public void getPracticeClassificationDetailsError() {
    given()
        .when().get("/session/2021/QAT/motogp/fp2/results/details")
        .then()
        .statusCode(500);
  }

  @Test
  public void getPracticeClassificationDetails() {
    List<PracticeClassificationDetails> details = given()
        .when().get("/session/2021/QAT/motogp/fp3/results/details")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<PracticeClassificationDetails>>() {
        });

    assertThat(details.size()).isEqualTo(22);
    assertThat(details.get(0).position).isEqualTo(1);
    assertThat(details.get(0).riderNumber).isEqualTo(21);
    assertThat(details.get(0).riderName).containsIgnoringCase("MORBIDELLI");
    assertThat(details.get(0).nation).isEqualTo("ITA");
    assertThat(details.get(0).team).containsIgnoringCase("Yamaha");
    assertThat(details.get(0).constructor).containsIgnoringCase("Yamaha");
    assertThat(details.get(0).totalLaps).isEqualTo(17);
    assertThat(details.get(0).gapToFirst).isEqualTo(0f);
    assertThat(details.get(0).bestLapTime).isEqualTo("1'54.921");
    assertThat(details.get(0).bestLapNumber).isEqualTo(16);
    assertThat(details.get(0).topSpeed).isEqualTo(342.8f);

    assertThat(details.get(1).constructor).isEqualTo("Aprilia");
    assertThat(details.get(1).gapToPrevious).isEqualTo(0.125f);
    assertThat(details.get(1).nation).isEqualTo("SPA");

    Assertions.assertTrue(details.stream().anyMatch(d -> d.position > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.riderNumber > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.riderName != null));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.nation.length() == 3));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.team != null));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.constructor != null));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.totalLaps > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.bestLapNumber > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.topSpeed > 0));
    Assertions.assertTrue(details.stream().anyMatch(d -> d.bestLapTime != null));

    float current = 0f;
    for (PracticeClassificationDetails d : details) {
      assertThat(d.gapToFirst).isGreaterThanOrEqualTo(current);
      current = d.gapToFirst;
    }
  }

  @Test
  public void getPracticeClassificationDetailsAsCsv() {
    String content = given()
        .when().get("/session/2021/QAT/motogp/fp3/results/details/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(23);
    assertThat(lines.get(0)).containsAnyOf("POSITION", "POINTS", "NUMBER", "NAME", "NATION", "TEAM", "CONSTRUCTOR", "TOTAL_TIME", "TOTAL_LAPS", "GAP_TO_FIRST", "AVERAGE_SPEED");
    assertThat(lines).anyMatch(s -> s.chars().filter(c -> c == ',').count() == 11);
    assertThat(lines).noneMatch(s -> s.contains("null") || s.contains("\"\""));
  }


  public Float topSpeed;

  @Test
  public void throwsErrorIfCantParsePdfWhenRaceClassificationDetails() {
    given()
        .when().get("/session/2021/QAT/motogp/FP2/topspeed")
        .then()
        .statusCode(500);
  }

  @Test
  public void getPracticeClassificationDetailsAsCsvFails() throws IOException {
    CsvConverter mock = Mockito.mock(CsvConverter.class);
    Mockito.when(mock.convertToCsv(Mockito.anyList(), Mockito.any())).thenThrow(new IOException("Nope"));
    QuarkusMock.installMockForType(mock, CsvConverter.class);
    given()
        .when().get("/session/2021/QAT/motogp/fp3/results/details/csv")
        .then()
        .statusCode(500);
  }

}
