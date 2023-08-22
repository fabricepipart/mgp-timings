package org.teknichrono.mgp.it.endpoints;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.SessionTopSpeedsOutput;
import org.teknichrono.mgp.it.WireMockExtensions;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestSessionTopSpeedsEndpoint {

  @Test
  @Tag("live")
  void parsesMaxSpeedPdf() {
    SessionTopSpeedsOutput speeds = given()
        .when().get("/api/2021/QAT/motogp/FP1/topspeed")
        .then()
        .statusCode(200).extract().as(SessionTopSpeedsOutput.class);

    assertThat(speeds.topSpeeds.size()).isEqualTo(22);
    assertThat(speeds.topSpeeds.get(0).speed).isEqualTo(354.0f);
    assertThat(speeds.topSpeeds.get(0).motorcycle).isEqualToIgnoringCase("Ducati");
    assertThat(speeds.topSpeeds.size()).isEqualTo(22);
    assertThat(speeds.topSpeeds.stream().filter(m -> m.number == 23).findFirst().get().team).isEqualTo("Avintia Esponsorama");

    assertThat(speeds.topSpeeds).anyMatch(s -> s.number != null &&
        s.rider != null &&
        s.nation != null &&
        s.team != null &&
        s.motorcycle != null &&
        s.speed != null);
  }

  @Test
  void getSessionTopSpeedsFailsBecauseOfSession() {
    given()
        .when().get("/api/2021/QAT/motogp/fp9/topspeed")
        .then()
        .statusCode(404);
  }

  @Test
  void getSessionTopSpeedsFailsBecauseOfEvent() {
    given()
        .when().get("/api/2021/NOP/motogp/fp3/topspeed")
        .then()
        .statusCode(404);
  }

  @Test
  void throwsErrorIfCantParsePdf() {
    given()
        .when().get("/api/2021/QAT/motogp/FP2/topspeed")
        .then()
        .statusCode(500);
  }

  @Test
  void throwsErrorIfNoPdfForTopSpeed() {
    given()
        .when().get("/api/2021/QAT/motogp/FP3/topspeed")
        .then()
        .statusCode(404);
  }


}
