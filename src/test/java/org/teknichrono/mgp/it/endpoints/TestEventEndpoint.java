package org.teknichrono.mgp.it.endpoints;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.EventOutput;
import org.teknichrono.mgp.it.WireMockExtensions;

import static io.restassured.RestAssured.given;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestEventEndpoint {

  @Test
  void getOneParticularEvent() {
    EventOutput event = given()
        .when().get(" /api/2021/QAT")
        .then()
        .statusCode(200).extract().as(EventOutput.class);
    Assertions.assertThat(event).isNotNull();
    Assertions.assertThat(event.short_name).isEqualToIgnoringCase("QAT");
    Assertions.assertThat(event.countryName).containsIgnoringCase("QA");
    Assertions.assertThat(event.eventFilesUrls).isNotEmpty();
    Assertions.assertThat(event.circuitName).containsIgnoringCase("Losail");
    Assertions.assertThat(event.categories).filteredOn(c -> c.name.contains("Moto3")).isNotEmpty();

  }

  @Test
  void getOneParticularTest() {
    EventOutput event = given()
        .when().get(" /api/2022/JE1")
        .then()
        .statusCode(200).extract().as(EventOutput.class);
    Assertions.assertThat(event).isNotNull();
    Assertions.assertThat(event.short_name).isEqualToIgnoringCase("JE1");
    Assertions.assertThat(event.countryName).containsIgnoringCase("ES");
    Assertions.assertThat(event.eventFilesUrls).isNotEmpty();
    Assertions.assertThat(event.circuitName).containsIgnoringCase("Jerez");
    Assertions.assertThat(event.categories).filteredOn(c -> c.name.contains("MotoGP")).isNotEmpty();
  }

  @Test
  void failToFindEvent() {
    given()
        .when().get(" /api/2021/NOP")
        .then()
        .statusCode(404);
  }

  @Test
  void failToFindEventBecauseOfYear() {
    given()
        .when().get(" /api/2299/NOP")
        .then()
        .statusCode(404);
  }
}
