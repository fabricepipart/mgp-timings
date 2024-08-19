package org.teknichrono.mgp.it.endpoints;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.CategoryRidersOutput;
import org.teknichrono.mgp.it.WireMockExtensions;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@Tag("integration")
@QuarkusTest
@WithTestResource(WireMockExtensions.class)
public class TestCategoryRidersEndpoint {


  @Test
  @Tag("live")
  void listRidersOfCategoryOfEvent() {
    CategoryRidersOutput riders = given()
        .when().get("/api/2021/QAT/GP/riders")
        .then()
        .statusCode(200).extract().as(CategoryRidersOutput.class);

    assertThat(riders.riders).isNotEmpty();
    assertThat(riders.riders).hasSizeGreaterThanOrEqualTo(22);
    // TODO Used to be 23 , now 22 because replacement Savadori is not returned anymore with a api UUID
    // We could get the info from the PDF Entry list (see EntryList.file)

    assertThat(riders.riders).anyMatch(rider -> rider.name != null &&
        rider.surname != null &&
        rider.country != null && rider.country.iso != null && rider.country.name != null &&
        rider.biography.text != null &&
        rider.birth_city != null &&
        rider.physical_attributes != null);
  }

  @Test
  void errorIfCategoryDoesNotExist() {
    given()
        .when().get("/api/2021/QAT/MOTO9/riders")
        .then()
        .statusCode(404);
  }

  @Test
  void errorIfCantFindRidersOfCategory() {
    given()
        .when().get("/api/2021/QAT/MOTO2/riders")
        .then()
        .statusCode(404);
  }


  @Test
  void errorIfCantFindYear() {
    given()
        .when().get("/api/12021/QAT/GP/riders")
        .then()
        .statusCode(404);
  }


}
