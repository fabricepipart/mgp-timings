package org.teknichrono.mgp.it.internal;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.api.model.SessionRider;
import org.teknichrono.mgp.client.model.result.Entry;
import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.client.model.result.SessionResults;
import org.teknichrono.mgp.csv.converter.CsvConverterFactory;
import org.teknichrono.mgp.csv.converter.CsvConverterInterface;
import org.teknichrono.mgp.it.WireMockExtensions;

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
@WithTestResource(WireMockExtensions.class)
public class TestInternalSessionEndpoint {

  @InjectSpy
  CsvConverterFactory csvFactory;

  @Test
  void listsAllSessionsOfCategoryOfEvent() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp")
        .then()
        .statusCode(200)
        .body("$.size()", is(8),
            "[0].id", is("117144ae-2a0b-4d42-8d89-ab96253470d2"),
            "[0].type", containsStringIgnoringCase("FP"),
            "[0].number", is(1),
            "[0].circuit", containsStringIgnoringCase("Lusail"));
  }

  @Test
  void listsAllSessionsOfCategoryOfTest() {
    given()
        .when().get("/api/internal/session/test/2022/JE1/GP")
        .then()
        .statusCode(200)
        .body("$.size()", is(2),
            "[0].id", is("baaef7a9-8f8c-4f5c-9e2d-40192824e66b"),
            "[0].type", containsStringIgnoringCase("FP"),
            "[0].number", is(1),
            "[0].circuit", containsStringIgnoringCase("Circuito de Jerez - Ángel Nieto"));
  }

  @Test
  void getSession() {
    String sessionString = given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3")
        .then()
        .statusCode(200).extract().asString();


    Session session = given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3")
        .then()
        .statusCode(200).extract().as(Session.class);

    assertThat(session.id).isNotNull();
    assertThat(session.test).isFalse();
    assertThat(session.type).isEqualTo("FP");
    assertThat(session.number).isEqualTo(3);
    assertThat(session.condition).isNotNull();
    assertThat(session.date).isNotNull();
    assertThat(session.circuit).isNotNull();
    assertThat(session.session_files).isNotEmpty();
  }

  @Test
  void getSessionFails() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/FP9")
        .then()
        .statusCode(404);
  }

  @Test
  void getTestSession() {
    Session session = given()
        .when().get("/api/internal/session/test/2022/JE1/GP/FP2")
        .then()
        .statusCode(200).extract().as(Session.class);

    assertThat(session.id).isNotNull();
    assertThat(session.test).isTrue();
    assertThat(session.type).isEqualTo("FP");
    assertThat(session.number).isEqualTo(2);
    assertThat(session.condition).isNotNull();
    assertThat(session.date).isNotNull();
    assertThat(session.circuit).isNotNull();
    assertThat(session.session_files).isNotEmpty();
  }


  @Test
  void listRidersFails() {
    given()
        .when().get("/api/internal/session/2021/NOP/GP/riders")
        .then()
        .statusCode(404);
  }


  @Test
  void listRidersOfCategoryOfEvent() {
    List<Entry> riders = given()
        .when().get("/api/internal/session/2021/QAT/GP/riders")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<Entry>>() {
        });
    assertThat(riders).isNotEmpty();
    assertThat(riders).hasSize(23);

    assertThat(riders).anyMatch(rider -> rider.rider.full_name != null &&
        rider.rider.country != null && rider.rider.country.iso != null && rider.rider.country.name != null);
  }

  @Test
  void listRidersDetailsOfCategoryOfEvent() {
    List<SessionRider> riders = given()
        .when().get("/api/internal/session/2021/QAT/GP/ridersdetails")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<SessionRider>>() {
        });
    assertThat(riders).isNotEmpty();
    assertThat(riders).hasSize(23);

    assertThat(riders).anyMatch(rider -> rider.name != null &&
        rider.surname != null &&
        rider.country != null && rider.country.iso != null && rider.country.name != null &&
        rider.biography.text != null &&
        rider.birth_city != null &&
        rider.physical_attributes != null);
  }


  @Test
  void listRidersDetailsFails() {
    given()
        .when().get("/api/internal/session/2021/NOP/GP/ridersdetails")
        .then()
        .statusCode(404);
  }

  @Test
  void parsesMaxSpeedPdf() {
    List<Map> speeds = given()
        .when().get("/api/internal/session/2021/QAT/motogp/FP1/topspeed")
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
  void throwsErrorIfCantParsePdf() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/FP2/topspeed")
        .then()
        .statusCode(500);
  }

  @Test
  void throwsErrorIfNoPdfForTopSpeed() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/FP3/topspeed")
        .then()
        .statusCode(404);
  }

  @Test
  void getSessionClassification() {
    SessionResults sessionResults = given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3/results")
        .then()
        .statusCode(200).extract().as(SessionResults.class);
    assertThat(sessionResults.classification.size()).isEqualTo(22);
    assertThat(sessionResults.classification.get(0).rider.full_name).containsIgnoringCase("Franco");
    assertThat(sessionResults.classification.get(0).total_laps).isEqualTo(17);
    assertThat(sessionResults.file).isNotNull();
    assertThat(sessionResults.records.stream().filter(r -> r.type.equalsIgnoreCase("bestLap")).findFirst().get().rider.full_name).containsIgnoringCase("Lorenzo");
  }

  @Test
  void getSessionClassificationFails() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp9/results")
        .then()
        .statusCode(404);
  }

  @Test
  void getTestSessionClassification() {
    SessionResults sessionResults = given()
        .when().get("/api/internal/session/test/2022/JE1/GP/FP2/results")
        .then()
        .statusCode(200).extract().as(SessionResults.class);
    assertThat(sessionResults.classification.size()).isEqualTo(29);
    assertThat(sessionResults.classification.get(0).rider.full_name).containsIgnoringCase("Francesco");
    assertThat(sessionResults.classification.get(0).total_laps).isEqualTo(42);
    assertThat(sessionResults.files).isNotNull();
  }


  @Test
  void getRaceClassification() {
    SessionResults sessionResults = given()
        .when().get("/api/internal/session/2021/QAT/motogp/rac/results")
        .then()
        .statusCode(200).extract().as(SessionResults.class);
    assertThat(sessionResults.classification.size()).isEqualTo(22);
    assertThat(sessionResults.classification.get(0).position).isEqualTo(1);
    assertThat(sessionResults.classification.get(0).total_laps).isEqualTo(22);
    assertThat(sessionResults.file).containsIgnoringCase("Classification.pdf");
    assertThat(sessionResults.records.stream().filter(r -> r.type.equalsIgnoreCase("poleLap")).findFirst().get().rider.full_name).containsIgnoringCase("Bagnaia");
  }


  @Test
  void getSessionClassificationAsCsv() {
    String content = given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3/results/csv")
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
  void getSessionClassificationAsCsvFails() throws IOException {
    CsvConverterInterface classificationCsvConverter = Mockito.mock(CsvConverterInterface.class);
    Mockito.when(csvFactory.getCsvConverter(RiderClassification.class)).thenReturn(classificationCsvConverter);
    Mockito.when(classificationCsvConverter.convertToCsv(Mockito.anyList())).thenThrow(new IOException("Nope"));
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3/results/csv")
        .then()
        .statusCode(500);
  }


  @Test
  void getRaceClassificationDetails() {
    List<SessionClassificationOutput> details = given()
        .when().get("/api/internal/session/2021/QAT/motogp/rac/results/details")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<SessionClassificationOutput>>() {
        });

    assertThat(details.size()).isEqualTo(22);
    assertThat(details.get(0).averageSpeed).isEqualTo(167.1f);
    assertThat(details.get(0).position).isEqualTo(1);
    assertThat(details.get(0).points).isEqualTo(25);
    assertThat(details.get(0).rider.country.iso).isEqualTo("SPA");
    assertThat(details.get(0).totalLaps).isEqualTo(22);
    assertThat(details.get(0).totalTime).isEqualTo("42'28.663");

    assertThat(details.get(1).constructor).containsIgnoringCase("Ducati");
    assertThat(details.get(1).rider.full_name).containsIgnoringCase("Zarco");

    Float currentGapToFirst = 0f;
    Integer currentPoints = Integer.MAX_VALUE;
    for (SessionClassificationOutput d : details) {
      assertThat(d.gapToFirst == null || d.gapToFirst.floatValue() >= currentGapToFirst).isTrue();
      if (currentGapToFirst == null) {
        assertThat(d.gapToFirst).isNull();
      } else {
        currentGapToFirst = d.gapToFirst;
      }
      assertThat(d.points == 0 || d.points.intValue() < currentPoints).isTrue();
      if (d.points == 0) {
        assertThat(d.points).isEqualTo(0);
      } else {
        currentPoints = d.points;
      }
    }

    assertTrue(details.stream().anyMatch(d -> d.position > 0));
    assertTrue(details.stream().anyMatch(d -> d.rider.number > 0));
    assertTrue(details.stream().anyMatch(d -> d.rider.full_name != null));
    assertTrue(details.stream().anyMatch(d -> d.rider.country.iso.length() == 3));
    assertTrue(details.stream().anyMatch(d -> d.team != null));
    assertTrue(details.stream().anyMatch(d -> d.constructor != null));
    assertTrue(details.stream().anyMatch(d -> d.totalLaps > 0));
    assertTrue(details.stream().anyMatch(d -> d.averageSpeed > 0));
    assertTrue(details.stream().anyMatch(d -> d.totalTime != null));
  }

  @Test
  void getRaceClassificationDetailsFails() {
    given()
        .when().get("/api/internal/session/2021/NOP/motogp/rac/results/details")
        .then()
        .statusCode(404);
  }

  @Test
  void getRaceClassificationDetailsAsCsv() {
    String content = given()
        .when().get("/api/internal/session/2021/QAT/motogp/rac/results/details/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(23);
    assertThat(lines.get(0)).containsAnyOf("POSITION", "POINTS", "NUMBER", "NAME", "NATION", "TEAM", "CONSTRUCTOR", "TOTAL_TIME", "TOTAL_LAPS", "GAP_TO_FIRST", "AVERAGE_SPEED");
    assertThat(lines).allMatch(s -> s.chars().filter(c -> c == ',').count() == 14);
  }


  @Test
  void getPracticeClassificationDetailsError() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp4/results/details")
        .then()
        .statusCode(500);
  }

  @Test
  void getPracticeClassificationDetails() {
    List<SessionClassificationOutput> details = given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3/results/details")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<SessionClassificationOutput>>() {
        });

    assertThat(details.size()).isEqualTo(22);
    assertThat(details.get(0).position).isEqualTo(1);
    assertThat(details.get(0).rider.number).isEqualTo(21);
    assertThat(details.get(0).rider.full_name).containsIgnoringCase("MORBIDELLI");
    assertThat(details.get(0).rider.country.iso).isEqualTo("ITA");
    assertThat(details.get(0).team).containsIgnoringCase("Yamaha");
    assertThat(details.get(0).constructor).containsIgnoringCase("Yamaha");
    assertThat(details.get(0).totalLaps).isEqualTo(17);
    assertThat(details.get(0).gapToFirst).isEqualTo(0f);
    assertThat(details.get(0).bestLapTime).isEqualTo("1'54.676");
    assertThat(details.get(0).bestLapNumber).isEqualTo(14);
    assertThat(details.get(0).topSpeed).isEqualTo(343.9f);

    assertThat(details.get(1).constructor).isEqualTo("Aprilia");
    assertThat(details.get(1).gapToPrevious).isEqualTo(0.165f);
    assertThat(details.get(1).rider.country.iso).isEqualTo("SPA");

    assertTrue(details.stream().allMatch(d -> d.position > 0));
    assertTrue(details.stream().allMatch(d -> d.rider.number > 0));
    assertTrue(details.stream().allMatch(d -> d.rider.full_name != null));
    assertTrue(details.stream().allMatch(d -> d.rider.country.iso.length() == 3));
    assertTrue(details.stream().allMatch(d -> d.team != null));
    assertTrue(details.stream().allMatch(d -> d.constructor != null));
    assertTrue(details.stream().allMatch(d -> d.totalLaps > 0));
    assertTrue(details.stream().allMatch(d -> d.bestLapNumber > 0));
    assertTrue(details.stream().allMatch(d -> d.topSpeed > 0));
    assertTrue(details.stream().allMatch(d -> d.bestLapTime != null));

    float current = 0f;
    for (SessionClassificationOutput d : details) {
      assertThat(d.gapToFirst).isGreaterThanOrEqualTo(current);
      current = d.gapToFirst;
    }
  }

  @Test
  void getTestClassificationDetails() {
    List<SessionClassificationOutput> details = given()
        .when().get("/api/internal/session/test/2022/JE1/GP/FP2/results/details")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<SessionClassificationOutput>>() {
        });


    assertThat(details.size()).isEqualTo(29);
    assertThat(details.get(0).position).isEqualTo(1);
    assertThat(details.get(0).rider.number).isEqualTo(63);
    assertThat(details.get(0).rider.full_name).containsIgnoringCase("BAGNAIA");
    assertThat(details.get(0).rider.country.iso).isEqualTo("ITA");
    assertThat(details.get(0).team).containsIgnoringCase("Ducati");
    assertThat(details.get(0).constructor).containsIgnoringCase("Ducati");
    assertThat(details.get(0).totalLaps).isEqualTo(42);
    assertThat(details.get(0).gapToFirst).isEqualTo(0f);
    assertThat(details.get(0).bestLapTime).isEqualTo("1'36.872");
    assertThat(details.get(0).bestLapNumber).isEqualTo(12);
    assertThat(details.get(0).topSpeed).isEqualTo(295.8f);

    assertThat(details.get(1).constructor).isEqualTo("Yamaha");
    assertThat(details.get(1).gapToPrevious).isEqualTo(0.452f);
    assertThat(details.get(1).rider.country.iso).isEqualTo("FRA");

    assertTrue(details.stream().allMatch(d -> d.position != null ? d.position > 0 : true));
    assertTrue(details.stream().allMatch(d -> d.rider.number > 0));
    assertTrue(details.stream().allMatch(d -> d.rider.full_name != null));
    assertTrue(details.stream().allMatch(d -> d.rider.country.iso.length() == 3));
    assertTrue(details.stream().allMatch(d -> d.team != null));
    assertTrue(details.stream().allMatch(d -> d.constructor != null));
    assertTrue(details.stream().allMatch(d -> d.totalLaps == null || d.totalLaps > 0));
    assertTrue(details.stream().allMatch(d -> d.bestLapNumber == null || d.bestLapNumber > 0));
    assertTrue(details.stream().allMatch(d -> d.topSpeed == null || d.topSpeed > 0));

    float current = 0f;
    for (SessionClassificationOutput d : details) {
      if (d.position != null) {
        assertThat(d.gapToFirst).isGreaterThanOrEqualTo(current);
        current = d.gapToFirst;
      } else {
        assertThat(d.gapToFirst).isNull();
        current = Float.MAX_VALUE;
      }
    }
  }


  @Test
  void getTestClassificationDetailsError() {
    given()
        .when().get("/api/internal/session/test/2022/MY1/GP/FP1/results/details")
        .then()
        .statusCode(500);
  }

  @Test
  void getPracticeClassificationDetailsAsCsv() {
    String content = given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3/results/details/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(23);
    assertThat(lines.get(0)).containsAnyOf("POSITION", "POINTS", "NUMBER", "NAME", "NATION", "TEAM", "CONSTRUCTOR", "TOTAL_TIME", "TOTAL_LAPS", "GAP_TO_FIRST", "AVERAGE_SPEED");
    assertThat(lines).allMatch(s -> s.chars().filter(c -> c == ',').count() == 14);
  }

  @Test
  void getTestPracticeClassificationDetailsAsCsv() {
    String content = given()
        .when().get("/api/internal/session/test/2022/JE1/motogp/fp2/results/details/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(30);
    assertThat(lines.get(0)).containsAnyOf("POSITION", "POINTS", "NUMBER", "NAME", "NATION", "TEAM", "CONSTRUCTOR", "TOTAL_TIME", "TOTAL_LAPS", "GAP_TO_FIRST", "AVERAGE_SPEED");
    assertThat(lines).allMatch(s -> s.chars().filter(c -> c == ',').count() == 14);
    assertThat(lines).anyMatch(s -> s.contains("Mika Kallio"));
    assertThat(lines).anyMatch(s -> s.contains("FIN"));
  }

  @Test
  void throwsErrorIfCantParsePdfWhenRaceClassificationDetails() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/FP2/topspeed")
        .then()
        .statusCode(500);
  }

  @Test
  void getPracticeClassificationDetailsAsCsvFails() throws IOException {
    CsvConverterInterface classificationCsvConverter = Mockito.mock(CsvConverterInterface.class);
    Mockito.when(csvFactory.getCsvConverter(SessionClassificationOutput.class)).thenReturn(classificationCsvConverter);
    Mockito.when(classificationCsvConverter.convertToCsv(Mockito.anyList())).thenThrow(new IOException("Nope"));
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3/results/details/csv")
        .then()
        .statusCode(500);
  }

  @Test
  void getTestPracticeClassificationDetailsAsCsvFails() throws IOException {
    CsvConverterInterface classificationCsvConverter = Mockito.mock(CsvConverterInterface.class);
    Mockito.when(csvFactory.getCsvConverter(SessionClassificationOutput.class)).thenReturn(classificationCsvConverter);
    Mockito.when(classificationCsvConverter.convertToCsv(Mockito.anyList())).thenThrow(new IOException("Nope"));
    given()
        .when().get("/api/internal/session/test/2022/JE1/motogp/fp2/results/details/csv")
        .then()
        .statusCode(500);
  }

  @Test
  void getTestAnalysis() {
    List<LapAnalysis> details = given()
        .when().get("/api/internal/session/test/2022/JE1/motogp/FP2/analysis")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<LapAnalysis>>() {
        });

    assertTrue(details.stream().allMatch(l -> l.number > 0));
    assertTrue(details.stream().allMatch(l -> l.rider != null));
    assertTrue(details.stream().allMatch(l -> l.nation.length() == 2));
    assertTrue(details.stream().allMatch(l -> l.team != null));
    assertTrue(details.stream().allMatch(l -> l.motorcycle != null));
    assertTrue(details.stream().allMatch(l -> l.lapNumber > 0));
    assertTrue(details.stream().anyMatch(l -> l.maxSpeed != null && l.maxSpeed > 0));
    assertTrue(details.stream().filter(l -> !l.unfinished.booleanValue()).allMatch(l -> l.time != null));
    assertTrue(details.stream().noneMatch(l -> l.frontTyre != null));
    assertTrue(details.stream().noneMatch(l -> l.backTyre != null));
    assertTrue(details.stream().noneMatch(l -> l.frontTyreLapNumber != null));
    assertTrue(details.stream().noneMatch(l -> l.backTyreLapNumber != null));
    assertTrue(details.stream().anyMatch(l -> l.cancelled));
    assertTrue(details.stream().anyMatch(l -> l.pit));
  }

  @Test
  void throwsErrorIfCantParseTestAnalysisPdf() {
    given()
        .when().get("/api/internal/session/test/2022/JE1/motogp/fp1/analysis")
        .then()
        .statusCode(500);
  }

  @Test
  void throws404IfCantFindTestAnalysis() {
    given()
        .when().get("/api/internal/session/test/2022/MY1/motogp/fp1/analysis")
        .then()
        .statusCode(404);
  }

  @Test
  void getPracticeAnalysis() {
    List<LapAnalysis> details = given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp3/analysis")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<LapAnalysis>>() {
        });

    assertThat(details.size()).isEqualTo(313);
    assertThat(details.get(0).number).isEqualTo(21);
    assertThat(details.get(0).rider).containsIgnoringCase("Morbidelli");
    assertThat(details.get(0).nation).isEqualTo("IT");
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
    assertTrue(details.stream().allMatch(l -> l.nation.length() == 2));
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
  void throwsErrorIfCantParseAnalysisPdf() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp2/analysis")
        .then()
        .statusCode(500);
  }

  @Test
  void throwsErrorIfNoAnalysisPdf() {
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/fp4/analysis")
        .then()
        .statusCode(404);
  }

  @Test
  void getRaceAnalysis() {
    List<LapAnalysis> details = given()
        .when().get("/api/internal/session/2021/QAT/motogp/RAC/analysis")
        .then()
        .statusCode(200).extract().as(new TypeRef<List<LapAnalysis>>() {
        });


    assertThat(details.size()).isEqualTo(439);
    assertThat(details.get(0).number).isEqualTo(12);
    assertThat(details.get(0).rider).containsIgnoringCase("Maverick");
    assertThat(details.get(0).nation).isEqualTo("ES");
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
    assertTrue(details.stream().allMatch(l -> l.nation.length() == 2));
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
  void getPracticeAnalysisAsCsv() {
    String content = given()
        .when().get("/api/internal/session/2021/QAT/motogp/RAC/analysis/csv")
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
  void getTestPracticeAnalysisAsCsv() {
    String content = given()
        .when().get("/api/internal/session/test/2022/JE1/motogp/FP2/analysis/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(1392);
    assertThat(lines.get(0)).containsAnyOf("NUMBER", "RIDER", "NATION", "TEAM", "MOTORCYCLE", "LAP_NUMBER", "TIME", "MAX_SPEED", "FRONT_TYRE", "BACK_TYRE", "FRONT_TYRE_LAP_NUMBER", "BACK_TYRE_LAP_NUMBER", "CANCELLED", "PIT", "UNFINISHED");
    assertThat(lines).allMatch(s -> s.chars().filter(c -> c == ',').count() == 14);
    assertThat(lines).noneMatch(s -> s.contains("null"));
    assertThat(lines).anyMatch(s -> s.contains("Ducati Lenovo Team"));
    assertThat(lines).anyMatch(s -> s.contains("ZA"));
    assertThat(lines).anyMatch(s -> s.contains("1:"));
  }

  @Test
  void getPracticeAnalysisAsCsvFails() throws IOException {
    CsvConverterInterface lapAnalysisCsvConverter = Mockito.mock(CsvConverterInterface.class);
    Mockito.when(csvFactory.getCsvConverter(LapAnalysis.class)).thenReturn(lapAnalysisCsvConverter);
    Mockito.when(lapAnalysisCsvConverter.convertToCsv(Mockito.anyList())).thenThrow(new IOException("Nope"));
    given()
        .when().get("/api/internal/session/2021/QAT/motogp/RAC/analysis/csv")
        .then()
        .statusCode(500);
  }

  @Test
  void getTestPracticeAnalysisAsCsvFails() throws IOException {
    CsvConverterInterface lapAnalysisCsvConverter = Mockito.mock(CsvConverterInterface.class);
    Mockito.when(csvFactory.getCsvConverter(LapAnalysis.class)).thenReturn(lapAnalysisCsvConverter);
    Mockito.when(lapAnalysisCsvConverter.convertToCsv(Mockito.anyList())).thenThrow(new IOException("Nope"));
    given()
        .when().get("/api/internal/session/test/2022/JE1/motogp/FP2/analysis/csv")
        .then()
        .statusCode(500);
  }

}
