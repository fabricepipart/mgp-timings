package org.teknichrono.mgp.it.endpoints;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.RootOutput;
import org.teknichrono.mgp.it.WireMockExtensions;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class TestRootEndpoint {


  @Test
  void listsAllSeasons() {
    RootOutput rootOutput = given()
        .when().get(" /api/")
        .then()
        .statusCode(200)
        .extract().as(RootOutput.class);
    assertThat(rootOutput).isNotNull();
    assertThat(rootOutput.years).isNotNull().hasSize(75);
    assertThat(rootOutput.years).contains(2022);
    assertThat(rootOutput.years).contains(2023);
    assertThat(rootOutput.years).contains(1949);
  }
}
