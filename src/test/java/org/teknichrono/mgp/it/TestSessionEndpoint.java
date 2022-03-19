package org.teknichrono.mgp.it;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
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
        .statusCode(400);
  }

  @Test
  public void throwsErrorIfNoPdf() {
    given()
        .when().get("/session/2021/QAT/motogp/FP3/topspeed")
        .then()
        .statusCode(404);
  }

}
