package org.teknichrono.rest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
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


}
