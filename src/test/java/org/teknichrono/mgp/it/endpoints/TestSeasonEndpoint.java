package org.teknichrono.mgp.it.endpoints;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.SeasonOutput;
import org.teknichrono.mgp.it.WireMockExtensions;

import static io.restassured.RestAssured.given;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestSeasonEndpoint {

  @Test
  void listsAllEvents() {
    SeasonOutput seasonOutput = given()
        .when().get(" /api/2021")
        .then()
        .statusCode(200)
        .extract().as(SeasonOutput.class);
    Assertions.assertThat(seasonOutput).isNotNull();
    Assertions.assertThat(seasonOutput.current).isFalse();
    Assertions.assertThat(seasonOutput.year).isEqualTo(2021);
    Assertions.assertThat(seasonOutput.races).hasSize(18);
    Assertions.assertThat(seasonOutput.races.get(0).shortName).isEqualTo("QAT");
    Assertions.assertThat(seasonOutput.races.get(0).name).containsIgnoringCase("Grand Prix of Qatar");
    Assertions.assertThat(seasonOutput.tests).hasSize(6);
    Assertions.assertThat(seasonOutput.tests.get(5).shortName).isEqualTo("MI1");
    Assertions.assertThat(seasonOutput.tests.get(5).name).containsIgnoringCase("Misano");
  }

  @Test
  void listsAllEventsOfSeasonThatDoesNotExist() {
    given().when().get(" /api/12021")
        .then()
        .statusCode(404);
  }

}
