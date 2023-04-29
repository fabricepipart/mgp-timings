package org.teknichrono.mgp.it.internal;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.it.WireMockExtensions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.is;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestInternalCategoryEndpoint {


  @Test
  public void listsAllCategoriesOfEvent() {
    given()
        .when().get("/api/internal/category/2021/QAT")
        .then()
        .statusCode(200)
        .body("$.size()", is(3),
            "[0].id", is("e8c110ad-64aa-4e8e-8a86-f2f152f6a942"),
            "[0].name", containsStringIgnoringCase("motogp")
        );
  }

  @Test
  public void containsThreeCategories() {
    given()
        .when().get("/api/internal/category/2021/ITA")
        .then()
        .body("$.size()", is(3),
            "[0].name", containsStringIgnoringCase("motogp"),
            "[1].name", containsStringIgnoringCase("moto2"),
            "[2].name", containsStringIgnoringCase("moto3")
        );
  }


  @Test
  public void listsAllCategoriesOfTest() {
    given()
        .when().get("/api/internal/category/test/2022/JE1")
        .then()
        .statusCode(200)
        .body("$.size()", is(1),
            "[0].id", is("e8c110ad-64aa-4e8e-8a86-f2f152f6a942"),
            "[0].name", containsStringIgnoringCase("motogp")
        );
  }


  @Test
  public void listsDetailsOfCategoryOfEvent() {
    Category cat = given()
        .when().get("/api/internal/category/2021/ITA/MOTO3")
        .then()
        .statusCode(200).extract().as(Category.class);
    Assertions.assertThat(cat.id).isNotNull();
    Assertions.assertThat(cat.name).containsIgnoringCase("moto3");
  }


  @Test
  public void listsDetailsOfCategoryOfTest() {
    Category cat = given()
        .when().get("/api/internal/category/test/2022/JE1/GP")
        .then()
        .statusCode(200).extract().as(Category.class);
    Assertions.assertThat(cat.id).isNotNull();
    Assertions.assertThat(cat.name).containsIgnoringCase("motogp");
  }


}
