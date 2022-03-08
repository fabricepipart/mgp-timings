package org.teknichrono.rest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.model.client.SessionClassification;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.containsString;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestClassificationEndpoint {


  @Test
  public void getSessionClassification() {
    SessionClassification classification = given()
        .when().get("/classification/2021/QAT/motogp/fp3")
        .then()
        .statusCode(200).extract().as(SessionClassification.class);
    Assertions.assertThat(classification.classification.size()).isEqualTo(22);
    Assertions.assertThat(classification.classification.get(0).rider.full_name).containsIgnoringCase("Franco");
    Assertions.assertThat(classification.classification.get(0).total_laps).isEqualTo(17);
    Assertions.assertThat(classification.file).isNotNull();
    Assertions.assertThat(classification.records.stream().filter(r -> r.type.equalsIgnoreCase("bestLap")).findFirst().get().rider.full_name).containsIgnoringCase("Marquez");
  }


  @Test
  public void getSessionClassificationAsCsv() {
    String content = given()
        .when().get("/classification/2021/QAT/motogp/fp3/csv")
        .then()
        .statusCode(200)
        .header("Content-Type", containsString("text/csv")).extract().asString();

    List<String> lines = content.lines().collect(Collectors.toList());

    assertThat(lines.size()).isEqualTo(23);
    assertThat(lines.get(0)).contains("BEST_LAP");
    assertThat(lines.get(0)).contains("RIDER");
    assertThat(lines.get(0)).contains("TOTAL_LAPS");
    assertThat(lines).anyMatch(s -> s.chars().filter(c -> c == ',').count() == 7);
    assertThat(lines).noneMatch(s -> s.contains("null"));
  }
}
