package org.teknichrono.rest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestEventEndpoint {


  @Test
  public void listsAllSeasons() {
    given()
        .when().get("/event/2021")
        .then()
        .statusCode(200)
        .body("$.size()", is(1),
            "[0].id", is("20bb257f-b1ba-4289-9030-c4eb528c6155")
        );
  }
}
