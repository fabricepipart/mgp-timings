package org.teknichrono.mgp.it;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    Assertions.assertThat(classification.classification.size()).isEqualTo(22);
    Assertions.assertThat(classification.classification.get(0).rider.full_name).containsIgnoringCase("Franco");
    Assertions.assertThat(classification.classification.get(0).total_laps).isEqualTo(17);
    Assertions.assertThat(classification.file).isNotNull();
    Assertions.assertThat(classification.records.stream().filter(r -> r.type.equalsIgnoreCase("bestLap")).findFirst().get().rider.full_name).containsIgnoringCase("Marquez");
  }


  @Test
  public void getRaceClassification() {
    SessionClassification classification = given()
        .when().get("/session/2021/QAT/motogp/rac/results")
        .then()
        .statusCode(200).extract().as(SessionClassification.class);
    Assertions.assertThat(classification.classification.size()).isEqualTo(22);
    Assertions.assertThat(classification.classification.get(0).position).isEqualTo(1);
    Assertions.assertThat(classification.classification.get(0).total_laps).isEqualTo(22);
    Assertions.assertThat(classification.file).containsIgnoringCase("Classification.pdf");
    Assertions.assertThat(classification.records.stream().filter(r -> r.type.equalsIgnoreCase("poleLap")).findFirst().get().rider.full_name).containsIgnoringCase("Bagnaia");
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
    given()
        .when().get("/session/2021/QAT/motogp/rac/results/details")
        .then()
        .statusCode(200)
        .body("$.size()", is(22),
            "[0].averageSpeed", is(167.1f),
            "[0].position", is(1),
            "[0].points", is(25),
            "[0].nation", is("SPA"),
            "[0].totalLaps", is(22),
            "[0].totalTime", is("42'28.663"),
            "[1].constructor", is("Ducati"),
            "[1].riderName", is("Johann Zarco"));
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
    given()
        .when().get("/session/2021/QAT/motogp/fp3/results/details")
        .then()
        .statusCode(200)
        .body("$.size()", is(22),
            "[0].position", is(1),
            "[0].riderNumber", is(21),
            "[0].riderName", containsStringIgnoringCase("MORBIDELLI"),
            "[0].nation", is("ITA"),
            "[0].team", containsStringIgnoringCase("Yamaha"),
            "[0].constructor", containsStringIgnoringCase("Yamaha"),
            "[0].totalLaps", is(17),
            "[0].gapToFirst", is(0f),
            "[0].bestLapTime", is("1'54.921"),
            "[0].bestLapNumber", is(16),
            "[0].topSpeed", is(342.8f),
            "[1].constructor", is("Aprilia"),
            "[1].gapToPrevious", is(0.125f),
            "[1].nation", is("SPA"));
  }


  public Float topSpeed;

  @Test
  public void throwsErrorIfCantParsePdfWhenRaceClassificationDetails() {
    given()
        .when().get("/session/2021/QAT/motogp/FP2/topspeed")
        .then()
        .statusCode(500);
  }

}
