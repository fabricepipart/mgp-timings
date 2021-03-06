package org.teknichrono.mgp.it;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestEventEndpoint {


  @Test
  public void listsAllEvents() {
    given()
        .when().get("/event/2021")
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
  public void listsAllEventsNames() {
    List<String> list = given()
        .when().get("/event/2021/names")
        .then()
        .statusCode(200).extract().as(List.class);
    assertThat(list.size()).isEqualTo(18);
    assertThat(list).anyMatch(s -> s.length() == 3);
  }
}
