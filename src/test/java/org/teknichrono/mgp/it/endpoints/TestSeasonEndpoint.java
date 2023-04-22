package org.teknichrono.mgp.it.endpoints;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.it.WireMockExtensions;
import org.teknichrono.mgp.model.output.SeasonOutput;

import static io.restassured.RestAssured.given;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestSeasonEndpoint {


  @Test
  public void listsAllEvents() {
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

/*
  @Test
  public void listsAllEvents() {
    given()
        .when().get(" /api/internal/event/2021")
        .then()
        .statusCode(200)
        .body("$.size()", is(18),
            "[0].id", is("20bb257f-b1ba-4289-9030-c4eb528c6155"),
            "[0].short_name", is("QAT"),
            "[0].country.iso", is("QA"),
            "[0].event_files.size()", is(5),
            "[0].circuit.place", is("Doha")
        );
  }


  @Test
  public void listsAllTests() {
    given()
        .when().get(" /api/internal/event/test/2022")
        .then()
        .statusCode(200)
        .body("$.size()", is(7),
            "[0].id", is("aacf14f8-fd7f-42d3-be6e-54add0eab84f"),
            "[0].short_name", is("JE1"),
            "[0].country.iso", is("ES"),
            "[0].event_files.size()", is(5),
            "[0].circuit.place", is("Jerez de la Frontera")
        );
  }
  */

}
