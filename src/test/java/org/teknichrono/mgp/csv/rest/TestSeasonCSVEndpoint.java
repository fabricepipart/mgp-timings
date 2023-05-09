package org.teknichrono.mgp.csv.rest;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.it.WireMockExtensions;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
class TestSeasonCSVEndpoint {

  @Test
  void listsAllSeasons() throws IOException, CsvException {
    String rootOutput = given()
        .when().get(" /api/csv")
        .then()
        .statusCode(200)
        .contentType("text/csv")
        .extract().asString();

    assertThat(rootOutput).isNotNull().isNotEmpty();
    try (CSVReader reader = new CSVReaderBuilder(new StringReader(rootOutput)).withSkipLines(1).build()) {
      List<String[]> allData = reader.readAll();
      assertThat(allData.stream().allMatch(l -> l.length == 2)).isTrue();
      assertThat(allData.stream().filter(l -> "true".equalsIgnoreCase(l[1])).collect(Collectors.toList())).hasSize(1);
      assertThat(allData.stream().filter(l -> "2022".equalsIgnoreCase(l[0])).collect(Collectors.toList())).hasSize(1);
      assertThat(allData.stream().filter(l -> "1949".equalsIgnoreCase(l[0])).collect(Collectors.toList())).hasSize(1);
    }
  }
}