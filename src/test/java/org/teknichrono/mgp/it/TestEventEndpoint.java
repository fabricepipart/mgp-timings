package org.teknichrono.mgp.it;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.model.result.Event;

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


  @Test
  public void listsAllTests() {
    given()
        .when().get("/event/test/2022")
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

  @Test
  public void listsAllTestsNames() {
    List<String> list = given()
        .when().get("/event/test/2022/names")
        .then()
        .statusCode(200).extract().as(List.class);
    assertThat(list.size()).isEqualTo(7);
    assertThat(list).anyMatch(s -> s.length() == 3);
  }

  @Test
  public void getOneParticularEvent() {
    Event event = given()
        .when().get("/event/2021/QAT")
        .then()
        .statusCode(200).extract().as(Event.class);
    Assertions.assertThat(event).isNotNull();
    Assertions.assertThat(event.id).isEqualToIgnoringCase("20bb257f-b1ba-4289-9030-c4eb528c6155");
    Assertions.assertThat(event.short_name).isEqualToIgnoringCase("QAT");
    Assertions.assertThat(event.country.iso).isEqualToIgnoringCase("QA");
    Assertions.assertThat(event.event_files).isNotEmpty();
    Assertions.assertThat(event.circuit.place).containsIgnoringCase("Doha");

  }

  @Test
  public void getOneParticularTest() {
    Event event = given()
        .when().get("/event/test/2022/JE1")
        .then()
        .statusCode(200).extract().as(Event.class);
    Assertions.assertThat(event).isNotNull();
    Assertions.assertThat(event.id).isEqualToIgnoringCase("aacf14f8-fd7f-42d3-be6e-54add0eab84f");
    Assertions.assertThat(event.short_name).isEqualToIgnoringCase("JE1");
    Assertions.assertThat(event.country.iso).isEqualToIgnoringCase("ES");
    Assertions.assertThat(event.event_files).isNotEmpty();
    Assertions.assertThat(event.circuit.place).containsIgnoringCase("Jerez");
  }
}
