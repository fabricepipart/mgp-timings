package org.teknichrono.mgp.it.endpoints;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.SessionAnalysisOutput;
import org.teknichrono.mgp.it.WireMockExtensions;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestSessionAnalysisEndpoint {


  @Test
  @Tag("live")
  void getTestAnalysis() {
    SessionAnalysisOutput analysis = given()
        .when().get("/api/2022/JE1/motogp/FP2/analysis")
        .then()
        .statusCode(200).extract().as(SessionAnalysisOutput.class);

    assertTrue(analysis.analysis.stream().allMatch(l -> l.number > 0));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.rider != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.nation.length() == 2));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.team != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.motorcycle != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.lapNumber > 0));
    assertTrue(analysis.analysis.stream().anyMatch(l -> l.maxSpeed != null && l.maxSpeed > 0));
    assertTrue(analysis.analysis.stream().filter(l -> !l.unfinished.booleanValue()).allMatch(l -> l.time != null));
    assertTrue(analysis.analysis.stream().noneMatch(l -> l.frontTyre != null));
    assertTrue(analysis.analysis.stream().noneMatch(l -> l.backTyre != null));
    assertTrue(analysis.analysis.stream().noneMatch(l -> l.frontTyreLapNumber != null));
    assertTrue(analysis.analysis.stream().noneMatch(l -> l.backTyreLapNumber != null));
    assertTrue(analysis.analysis.stream().anyMatch(l -> l.cancelled));
    assertTrue(analysis.analysis.stream().anyMatch(l -> l.pit));
  }


  @Test
  void getSessionAnalysisFailsBecauseOfSession() {
    given()
        .when().get("/api/2021/QAT/motogp/fp9/analysis")
        .then()
        .statusCode(404);
  }

  @Test
  void getSessionAnalysisFailsBecauseOfEvent() {
    given()
        .when().get("/api/2021/NOP/motogp/fp3/analysis")
        .then()
        .statusCode(404);
  }

  @Test
  void throwsErrorIfCantParseTestAnalysisPdf() {
    given()
        .when().get("/api/2022/JE1/motogp/fp1/analysis")
        .then()
        .statusCode(500);
  }


  @Test
  @Tag("live")
  void getPracticeAnalysis() {
    SessionAnalysisOutput analysis = given()
        .when().get("/api/2021/QAT/motogp/fp3/analysis")
        .then()
        .statusCode(200).extract().as(SessionAnalysisOutput.class);

    assertThat(analysis.analysis.size()).isEqualTo(313);
    assertThat(analysis.analysis.get(0).number).isEqualTo(21);
    assertThat(analysis.analysis.get(0).rider).containsIgnoringCase("Morbidelli");
    assertThat(analysis.analysis.get(0).nation).isEqualTo("IT");
    assertThat(analysis.analysis.get(0).team).containsIgnoringCase("Yamaha");
    assertThat(analysis.analysis.get(0).motorcycle).containsIgnoringCase("Yamaha");
    assertThat(analysis.analysis.get(0).lapNumber).isEqualTo(1);
    assertThat(analysis.analysis.get(0).time).isNotNull();
    assertThat(analysis.analysis.get(0).maxSpeed).isEqualTo(171.1f);
    assertThat(analysis.analysis.get(0).frontTyre).isEqualTo("Slick-Hard");
    assertThat(analysis.analysis.get(0).backTyre).isEqualTo("Slick-Hard");
    assertThat(analysis.analysis.get(0).frontTyreLapNumber).isEqualTo(0);
    assertThat(analysis.analysis.get(0).backTyreLapNumber).isEqualTo(0);
    assertThat(analysis.analysis.get(0).cancelled).isEqualTo(false);
    assertThat(analysis.analysis.get(0).pit).isEqualTo(false);

    assertTrue(analysis.analysis.stream().allMatch(l -> l.number > 0));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.rider != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.nation.length() == 2));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.team != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.motorcycle != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.lapNumber > 0));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.maxSpeed > 0));
    assertTrue(analysis.analysis.stream().filter(l -> !l.unfinished.booleanValue()).allMatch(l -> l.time != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.frontTyre != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.backTyre != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.frontTyreLapNumber >= 0));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.backTyreLapNumber >= 0));
    assertTrue(analysis.analysis.stream().anyMatch(l -> l.cancelled));
    assertTrue(analysis.analysis.stream().anyMatch(l -> l.pit));

    assertEquals(2, analysis.analysis.stream().filter(l -> l.unfinished.booleanValue()).collect(Collectors.toList()).size());

    List<LapAnalysis> mariniLaps = analysis.analysis.stream().filter(l -> l.rider.contains("Marini")).collect(Collectors.toList());
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
    List<LapAnalysis> bagnaiaLaps = analysis.analysis.stream().filter(l -> l.rider.contains("bagnaia")).collect(Collectors.toList());
    assertTrue(bagnaiaLaps.stream().allMatch(l -> l.backTyreLapNumber.intValue() != l.frontTyreLapNumber.intValue()));
    assertTrue(bagnaiaLaps.stream().noneMatch(l -> l.backTyre.equalsIgnoreCase(l.frontTyre)));

  }


  @Test
  @Tag("live")
  void getRaceAnalysis() {
    SessionAnalysisOutput analysis = given()
        .when().get("/api/2021/QAT/motogp/RAC/analysis")
        .then()
        .statusCode(200).extract().as(SessionAnalysisOutput.class);

    assertThat(analysis.analysis.size()).isEqualTo(439);
    assertThat(analysis.analysis.get(0).number).isEqualTo(12);
    assertThat(analysis.analysis.get(0).rider).containsIgnoringCase("Maverick");
    assertThat(analysis.analysis.get(0).nation).isEqualTo("ES");
    assertThat(analysis.analysis.get(0).team).containsIgnoringCase("Yamaha");
    assertThat(analysis.analysis.get(0).motorcycle).containsIgnoringCase("Yamaha");
    assertThat(analysis.analysis.get(0).lapNumber).isEqualTo(1);
    assertThat(analysis.analysis.get(0).time).isNotNull();
    assertThat(analysis.analysis.get(0).maxSpeed).isEqualTo(192.8f);
    assertThat(analysis.analysis.get(0).frontTyre).isEqualTo("Slick-Soft");
    assertThat(analysis.analysis.get(0).backTyre).isEqualTo("Slick-Soft");
    assertThat(analysis.analysis.get(0).frontTyreLapNumber).isEqualTo(0);
    assertThat(analysis.analysis.get(0).backTyreLapNumber).isEqualTo(0);
    assertThat(analysis.analysis.get(0).cancelled).isEqualTo(false);
    assertThat(analysis.analysis.get(0).pit).isEqualTo(false);
    assertThat(analysis.analysis.get(0).pit).isEqualTo(false);

    assertTrue(analysis.analysis.stream().allMatch(l -> l.number > 0));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.rider != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.nation.length() == 2));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.team != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.motorcycle != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.lapNumber > 0));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.maxSpeed > 0));
    assertTrue(analysis.analysis.stream().filter(l -> !l.unfinished.booleanValue()).allMatch(l -> l.time != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.frontTyre != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.backTyre != null));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.frontTyreLapNumber >= 0));
    assertTrue(analysis.analysis.stream().allMatch(l -> l.backTyreLapNumber >= 0));
    assertTrue(analysis.analysis.stream().anyMatch(l -> l.cancelled));
    assertTrue(analysis.analysis.stream().anyMatch(l -> l.unfinished));

    assertEquals(2, analysis.analysis.stream().filter(l -> l.unfinished.booleanValue()).collect(Collectors.toList()).size());

    List<LapAnalysis> mariniLaps = analysis.analysis.stream().filter(l -> l.rider.contains("Marini")).collect(Collectors.toList());
    assertThat(mariniLaps.size()).isEqualTo(22);
    List<Integer> frontTyreLaps = mariniLaps.stream().map(l -> l.frontTyreLapNumber).collect(Collectors.toList());
    List<Integer> rearTyreLaps = mariniLaps.stream().map(l -> l.backTyreLapNumber).collect(Collectors.toList());
    for (int i = 0; i <= 21; i++) {
      assertTrue(frontTyreLaps.contains(Integer.valueOf(i)));
      assertTrue(rearTyreLaps.contains(Integer.valueOf(i)));
    }
    List<LapAnalysis> bagnaiaLaps = analysis.analysis.stream().filter(l -> l.rider.contains("bagnaia")).collect(Collectors.toList());
    assertTrue(bagnaiaLaps.stream().allMatch(l -> l.backTyreLapNumber.intValue() != l.frontTyreLapNumber.intValue()));
    assertTrue(bagnaiaLaps.stream().noneMatch(l -> l.backTyre.equalsIgnoreCase(l.frontTyre)));
  }


}
