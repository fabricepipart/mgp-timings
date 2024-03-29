package org.teknichrono.mgp.it.endpoints;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.CategoryOutput;
import org.teknichrono.mgp.it.WireMockExtensions;

import static io.restassured.RestAssured.given;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestCategoryEndpoint {


  @Test
  @Tag("live")
  void listsAllSessionsOfCategoryOfEvent() {
    CategoryOutput categoryOutput = given()
        .when().get("/api/2021/QAT/motogp")
        .then()
        .statusCode(200)
        .extract().as(CategoryOutput.class);
    Assertions.assertThat(categoryOutput.name).containsIgnoringCase("MOTOGP");
    Assertions.assertThat(categoryOutput.sessions).hasSize(8);
    Assertions.assertThat(categoryOutput.sessions.get(0).shortName).isEqualTo("FP1");

  }

  @Test
  void errorIfCategoryDoesNotExist() {
    given()
        .when().get("/api/2021/QAT/MOTO9")
        .then()
        .statusCode(404);
  }

  @Test
  void errorIfYearDoesNotExist() {
    given()
        .when().get("/api/12021/QAT/MOTOGP")
        .then()
        .statusCode(404);
  }

  @Test
  void listsAllSessionsOfCategoryOfTest() {
    CategoryOutput categoryOutput = given()
        .when().get("/api/2022/JE1/GP")
        .then()
        .statusCode(200)
        .extract().as(CategoryOutput.class);
    Assertions.assertThat(categoryOutput.name).containsIgnoringCase("MOTOGP");
    Assertions.assertThat(categoryOutput.sessions).hasSize(2);
    Assertions.assertThat(categoryOutput.sessions.get(0).shortName).isEqualTo("FP1");
  }

}
