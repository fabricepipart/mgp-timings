package org.teknichrono.mgp.it;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class WireMockExtensions implements QuarkusTestResourceLifecycleManager {

  private WireMockServer wireMockServer;

  @Override
  public Map<String, String> start() {
    wireMockServer = new WireMockServer(8089);
    wireMockServer.start();
    configureFor(8089);

    stubSeasons();
    stubEvents();
    stubCategories();
    stubSessions();
    stubTestSessions();
    stubClassifications();
    stubTestClassifications();

    stubRiders();
    //stubFor(get(urlMatching(".*")).atPriority(10).willReturn(aResponse().proxiedFrom("http://localhost:8089/api/results-front/be/results-api")));

    Map<String, String> config = new HashMap<>();
    config.put("quarkus.rest-client.results-api.url", wireMockServer.baseUrl());
    config.put("quarkus.rest-client.riders-api.url", wireMockServer.baseUrl());

    return config;
  }

  private void stubRiders() {
    String path = Thread.currentThread().getContextClassLoader().getResource("__files/rider").getPath();
    File[] ridersFiles = new File(path).listFiles();
    for (File f : ridersFiles) {
      String filename = f.getName();
      String riderNumber = filename.replaceAll(".json", "");
      stubFor(get(urlEqualTo("/riders/" + riderNumber))
          .willReturn(aResponse()
              .withHeader("Content-Type", "application/json")
              .withBodyFile("rider/" + filename)));
    }
  }

  private void stubSessions() {
    stubFor(get(urlEqualTo("/event/20bb257f-b1ba-4289-9030-c4eb528c6155/category/e8c110ad-64aa-4e8e-8a86-f2f152f6a942/sessions"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("sessions.json")));
    stubFor(get(urlEqualTo("/event/20bb257f-b1ba-4289-9030-c4eb528c6155/category/e8c110ad-64aa-4e8e-8a86-f2f152f6a942/entry"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("entry.json")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP1/MaximumSpeed.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/MaximumSpeed.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP1/Analysis.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Analysis.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP3/Analysis.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Analysis-fp3.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP2/CorruptedMaximumSpeed.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/BestPartialTime.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/RAC/Analysis.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Analysis-rac.pdf")));
  }

  private void stubTestSessions() {
    stubFor(get(urlEqualTo("/event/3e7c342a-e406-470f-956f-d685be970bf4/category/e8c110ad-64aa-4e8e-8a86-f2f152f6a942/sessions"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("sessions-test-2022-my1-gp.json")));
    stubFor(get(urlEqualTo("/event/aacf14f8-fd7f-42d3-be6e-54add0eab84f/category/e8c110ad-64aa-4e8e-8a86-f2f152f6a942/sessions"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("sessions-test-2022-je1-gp.json")));
    stubFor(get(urlEqualTo("/files/testresults/2022_JEREZ_MotoGP____OFFICIAL_TEST_classification_2.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/2022_JEREZ_MotoGP____OFFICIAL_TEST_classification_2.pdf")));
    stubFor(get(urlEqualTo("/files/testresults/2022_JEREZ_MotoGP____OFFICIAL_TEST_analysis_2.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/2022_JEREZ_MotoGP____OFFICIAL_TEST_analysis_2.pdf")));
    stubFor(get(urlEqualTo("/files/testresults/2022_JEREZ_MotoGP____OFFICIAL_TEST_analysis_1.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/empty.pdf")));

  }

  private void stubClassifications() {
    stubFor(get(urlEqualTo("/session/b239e98b-9739-4fe3-ab6e-a6c3f5348226/classifications"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-qat-21-fp4-gp.json")));
    stubFor(get(urlEqualTo("/session/fae273c4-defb-4bac-84c8-e3283c5b088b/classifications"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications.json")));
    stubFor(get(urlEqualTo("/session/cfa834e4-91e8-458c-a12e-fc628dd071bf/classifications"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-rac.json")));
    stubFor(get(urlEqualTo("/session/64e9d65b-12cd-4436-8b63-549ac516bf02/classifications"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classification-fp2.json")));
    stubFor(get(urlEqualTo("/session/117144ae-2a0b-4d42-8d89-ab96253470d2/classifications"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-qat-21-fp1-gp.json")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/RAC/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Classification-rac.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP1/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Classification-fp1.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP3/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Classification-qat-mgp-fp3.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP4/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/empty.pdf")));
  }


  private void stubTestClassifications() {
    stubFor(get(urlEqualTo("/session/7aed8f0a-10b4-4a0e-9ef8-964f34687718/test-classifications"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-test-je1-gp-fp2.json")));
    stubFor(get(urlEqualTo("/session/baaef7a9-8f8c-4f5c-9e2d-40192824e66b/test-classifications"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-test-je1-gp-fp1.json")));
    stubFor(get(urlEqualTo("/session/b272fd1a-53f2-449b-b51b-ec222de9f9ed/test-classifications"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-test-my1-gp-fp1.json")));
    stubFor(get(urlEqualTo("/files/testresults/2022_SEPANG_MotoGP____OFFICIAL_TEST_classification_1.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("empty.json")));


  }


  private void stubSeasons() {
    stubFor(get(urlEqualTo("/seasons"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("seasons.json")));

    stubFor(get(urlEqualTo("/seasons?test=true"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("tests.json")));
  }

  private void stubEvents() {
    stubFor(get(urlEqualTo("/season/db8dc197-c7b2-4c1b-b3a4-7dc723c087ed/events"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("events.json")));
    stubFor(get(urlEqualTo("/season/db8dc197-c7b2-4c1b-b3a4-6dc534c014ef/events"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("events-2022.json")));
    stubFor(get(urlEqualTo("/season/db8dc197-c7b2-4c1b-b3a4-6dc534c014ef/events?test=true"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("events-tests-2022.json")));
  }

  private void stubCategories() {
    stubFor(get(urlEqualTo("/event/20bb257f-b1ba-4289-9030-c4eb528c6155/categories"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("categories.json")));
    stubFor(get(urlEqualTo("/event/0d106a9a-95c9-4e13-9084-90c78c149f4a/categories"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("category-ita.json")));
    stubFor(get(urlEqualTo("/event/aacf14f8-fd7f-42d3-be6e-54add0eab84f/categories"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("category-JE1-2022.json")));
    stubFor(get(urlEqualTo("/event/3e7c342a-e406-470f-956f-d685be970bf4/categories"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("category-MY1-2022.json")));
  }

  @Override
  public void stop() {
    if (null != wireMockServer) {
      wireMockServer.stop();
    }
  }
}