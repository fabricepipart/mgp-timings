package org.teknichrono.mgp.it;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.teknichrono.mgp.model.out.LapAnalysis;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    assertTrue(details.stream().anyMatch(d -> d.position > 0));
    assertTrue(details.stream().anyMatch(d -> d.riderNumber > 0));
    assertTrue(details.stream().anyMatch(d -> d.riderName != null));
    assertTrue(details.stream().anyMatch(d -> d.nation.length() == 3));
    assertTrue(details.stream().anyMatch(d -> d.team != null));
    assertTrue(details.stream().anyMatch(d -> d.constructor != null));
    assertTrue(details.stream().anyMatch(d -> d.totalLaps > 0));
    assertTrue(details.stream().anyMatch(d -> d.averageSpeed > 0));
    assertTrue(details.stream().anyMatch(d -> d.totalTime != null));
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
    assertThat(lines).allMatch(s -> s.chars().filter(c -> c == ',').count() == 10);
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

    assertTrue(details.stream().allMatch(d -> d.position > 0));
    assertTrue(details.stream().allMatch(d -> d.riderNumber > 0));
    assertTrue(details.stream().allMatch(d -> d.riderName != null));
    assertTrue(details.stream().allMatch(d -> d.nation.length() == 3));
    assertTrue(details.stream().allMatch(d -> d.team != null));
    assertTrue(details.stream().allMatch(d -> d.constructor != null));
    assertTrue(details.stream().allMatch(d -> d.totalLaps > 0));
    assertTrue(details.stream().allMatch(d -> d.bestLapNumber > 0));
    assertTrue(details.stream().allMatch(d -> d.topSpeed > 0));
    assertTrue(details.stream().allMatch(d -> d.bestLapTime != null));

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
    assertThat(lines).allMatch(s -> s.chars().filter(c -> c == ',').count() == 11);
    assertThat(lines).noneMatch(s -> s.contains("null") || s.contains("\"\""));
  }

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

  @Test
  public void getPracticeAnalysis() {
    List<LapAnalysis> details = given()
        .when().get("/session/2021/QAT/motogp/fp3/analysis")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<LapAnalysis>>() {
        });

    assertThat(details.size()).isEqualTo(313);
    assertThat(details.get(0).number).isEqualTo(21);
    assertThat(details.get(0).rider).containsIgnoringCase("Morbidelli");
    assertThat(details.get(0).nation).isEqualTo("ITA");
    assertThat(details.get(0).team).containsIgnoringCase("Yamaha");
    assertThat(details.get(0).motorcycle).containsIgnoringCase("Yamaha");
    assertThat(details.get(0).lapNumber).isEqualTo(1);
    assertThat(details.get(0).time).isNotNull();
    assertThat(details.get(0).maxSpeed).isEqualTo(171.1f);
    assertThat(details.get(0).frontTyre).isEqualTo("Slick-Hard");
    assertThat(details.get(0).backTyre).isEqualTo("Slick-Hard");
    assertThat(details.get(0).frontTyreLapNumber).isEqualTo(0);
    assertThat(details.get(0).backTyreLapNumber).isEqualTo(0);
    assertThat(details.get(0).cancelled).isEqualTo(false);
    assertThat(details.get(0).pit).isEqualTo(false);

    assertTrue(details.stream().allMatch(l -> l.number > 0));
    assertTrue(details.stream().allMatch(l -> l.rider != null));
    assertTrue(details.stream().allMatch(l -> l.nation.length() == 3));
    assertTrue(details.stream().allMatch(l -> l.team != null));
    assertTrue(details.stream().allMatch(l -> l.motorcycle != null));
    assertTrue(details.stream().allMatch(l -> l.lapNumber > 0));
    assertTrue(details.stream().allMatch(l -> l.maxSpeed > 0));
    assertTrue(details.stream().filter(l -> !l.unfinished.booleanValue()).allMatch(l -> l.time != null));
    assertTrue(details.stream().allMatch(l -> l.frontTyre != null));
    assertTrue(details.stream().allMatch(l -> l.backTyre != null));
    assertTrue(details.stream().allMatch(l -> l.frontTyreLapNumber >= 0));
    assertTrue(details.stream().allMatch(l -> l.backTyreLapNumber >= 0));
    assertTrue(details.stream().anyMatch(l -> l.cancelled));
    assertTrue(details.stream().anyMatch(l -> l.pit));

    assertEquals(2, details.stream().filter(l -> l.unfinished.booleanValue()).collect(Collectors.toList()).size());

    List<LapAnalysis> mariniLaps = details.stream().filter(l -> l.rider.contains("Marini")).collect(Collectors.toList());
    assertThat(mariniLaps.size()).isEqualTo(16);
    List<Integer> frontTyreLaps = mariniLaps.stream().map(l -> l.frontTyreLapNumber).collect(Collectors.toList());
    List<Integer> rearTyreLaps = mariniLaps.stream().map(l -> l.backTyreLapNumber).collect(Collectors.toList());
    for (int i = 5; i <= 16; i++) {
      assertTrue(frontTyreLaps.contains(Integer.valueOf(i)));
      assertTrue(rearTyreLaps.contains(Integer.valueOf(i)));
    }
    for (int i = 0; i <= 3; i++) {
      assertTrue(frontTyreLaps.contains(Integer.valueOf(i)));
      assertTrue(rearTyreLaps.contains(Integer.valueOf(i)));
    }
    List<LapAnalysis> bagnaiaLaps = details.stream().filter(l -> l.rider.contains("bagnaia")).collect(Collectors.toList());
    assertTrue(bagnaiaLaps.stream().allMatch(l -> l.backTyreLapNumber.intValue() != l.frontTyreLapNumber.intValue()));
    assertTrue(bagnaiaLaps.stream().noneMatch(l -> l.backTyre.equalsIgnoreCase(l.frontTyre)));

  }

  @Test
  public void throwsErrorIfCantParseAnalysisPdf() {
    given()
        .when().get("/session/2021/QAT/motogp/fp2/analysis")
        .then()
        .statusCode(500);
  }

  @Test
  public void throwsErrorIfNoAnalysisPdf() {
    given()
        .when().get("/session/2021/QAT/motogp/fp4/analysis")
        .then()
        .statusCode(404);
  }

  @Test
  public void getRaceAnalysis() {
    List<LapAnalysis> details = given()
        .when().get("/session/2021/QAT/motogp/RAC/analysis")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<LapAnalysis>>() {
        });


    assertThat(details.size()).isEqualTo(439);
    assertThat(details.get(0).number).isEqualTo(12);
    assertThat(details.get(0).rider).containsIgnoringCase("Maverick");
    assertThat(details.get(0).nation).isEqualTo("SPA");
    assertThat(details.get(0).team).containsIgnoringCase("Yamaha");
    assertThat(details.get(0).motorcycle).containsIgnoringCase("Yamaha");
    assertThat(details.get(0).lapNumber).isEqualTo(1);
    assertThat(details.get(0).time).isNotNull();
    assertThat(details.get(0).maxSpeed).isEqualTo(192.8f);
    assertThat(details.get(0).frontTyre).isEqualTo("Slick-Soft");
    assertThat(details.get(0).backTyre).isEqualTo("Slick-Soft");
    assertThat(details.get(0).frontTyreLapNumber).isEqualTo(0);
    assertThat(details.get(0).backTyreLapNumber).isEqualTo(0);
    assertThat(details.get(0).cancelled).isEqualTo(false);
    assertThat(details.get(0).pit).isEqualTo(false);
    assertThat(details.get(0).pit).isEqualTo(false);

    assertTrue(details.stream().allMatch(l -> l.number > 0));
    assertTrue(details.stream().allMatch(l -> l.rider != null));
    assertTrue(details.stream().allMatch(l -> l.nation.length() == 3));
    assertTrue(details.stream().allMatch(l -> l.team != null));
    assertTrue(details.stream().allMatch(l -> l.motorcycle != null));
    assertTrue(details.stream().allMatch(l -> l.lapNumber > 0));
    assertTrue(details.stream().allMatch(l -> l.maxSpeed > 0));
    assertTrue(details.stream().filter(l -> !l.unfinished.booleanValue()).allMatch(l -> l.time != null));
    assertTrue(details.stream().allMatch(l -> l.frontTyre != null));
    assertTrue(details.stream().allMatch(l -> l.backTyre != null));
    assertTrue(details.stream().allMatch(l -> l.frontTyreLapNumber >= 0));
    assertTrue(details.stream().allMatch(l -> l.backTyreLapNumber >= 0));
    assertTrue(details.stream().anyMatch(l -> l.cancelled));
    assertTrue(details.stream().anyMatch(l -> l.unfinished));

    assertEquals(2, details.stream().filter(l -> l.unfinished.booleanValue()).collect(Collectors.toList()).size());

    List<LapAnalysis> mariniLaps = details.stream().filter(l -> l.rider.contains("Marini")).collect(Collectors.toList());
    assertThat(mariniLaps.size()).isEqualTo(22);
    List<Integer> frontTyreLaps = mariniLaps.stream().map(l -> l.frontTyreLapNumber).collect(Collectors.toList());
    List<Integer> rearTyreLaps = mariniLaps.stream().map(l -> l.backTyreLapNumber).collect(Collectors.toList());
    for (int i = 0; i <= 21; i++) {
      assertTrue(frontTyreLaps.contains(Integer.valueOf(i)));
      assertTrue(rearTyreLaps.contains(Integer.valueOf(i)));
    }
    List<LapAnalysis> bagnaiaLaps = details.stream().filter(l -> l.rider.contains("bagnaia")).collect(Collectors.toList());
    assertTrue(bagnaiaLaps.stream().allMatch(l -> l.backTyreLapNumber.intValue() != l.frontTyreLapNumber.intValue()));
    assertTrue(bagnaiaLaps.stream().noneMatch(l -> l.backTyre.equalsIgnoreCase(l.frontTyre)));
  }

  @Test
  public void getPracticeAnalysisAsCsv() {
    String content = given()
        .when().get("/session/2021/QAT/motogp/RAC/analysis/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(440);
    assertThat(lines.get(0)).containsAnyOf("NUMBER", "RIDER", "NATION", "TEAM", "MOTORCYCLE", "LAP_NUMBER", "TIME", "MAX_SPEED", "FRONT_TYRE", "BACK_TYRE", "FRONT_TYRE_LAP_NUMBER", "BACK_TYRE_LAP_NUMBER", "CANCELLED", "PIT", "UNFINISHED");
    assertThat(lines).allMatch(s -> s.chars().filter(c -> c == ',').count() == 14);
    assertThat(lines).noneMatch(s -> s.contains("null"));
    assertThat(lines.stream().filter(s -> s.contains("\"\"")).collect(Collectors.toList()).size()).isEqualTo(2);
  }

  @Test
  public void getPracticeAnalysisAsCsvFails() throws IOException {
    CsvConverter mock = Mockito.mock(CsvConverter.class);
    Mockito.when(mock.convertToCsv(Mockito.anyList(), Mockito.any())).thenThrow(new IOException("Nope"));
    QuarkusMock.installMockForType(mock, CsvConverter.class);
    given()
        .when().get("/session/2021/QAT/motogp/RAC/analysis/csv")
        .then()
        .statusCode(500);
  }

}
