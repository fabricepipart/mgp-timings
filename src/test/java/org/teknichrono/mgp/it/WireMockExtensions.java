package org.teknichrono.mgp.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.teknichrono.mgp.client.model.rider.RiderDetails;

import java.io.File;
import java.io.IOException;
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
    if ("false".equals(System.getProperty("mgp-timings.mocking"))) {
      return Map.of();
    }

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
      try {
        RiderDetails rider = new ObjectMapper().readValue(f, RiderDetails.class);
        stubFor(get(urlEqualTo("/" + rider.id))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("rider/" + filename)));

        stubFor(get(urlEqualTo("/" + riderNumber))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("rider/" + filename)));

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void stubSessions() {
    stubFor(get(urlEqualTo("/sessions?eventUuid=20bb257f-b1ba-4289-9030-c4eb528c6155&categoryUuid=e8c110ad-64aa-4e8e-8a86-f2f152f6a942"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("sessions.json")));
    stubFor(get(urlEqualTo("/sessions?eventUuid=0d106a9a-95c9-4e13-9084-90c78c149f4a&categoryUuid=954f7e65-2ef2-4423-b949-4961cc603e45"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("sessions-ita-2021-m3.json")));
    stubFor(get(urlEqualTo("/sessions?eventUuid=31751038-df46-40f8-84ac-2e72ed604550&categoryUuid=e8c110ad-64aa-4e8e-8a86-f2f152f6a942"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("sessions-arg-2014-mgp.json")));
    stubFor(get(urlEqualTo("/event/20bb257f-b1ba-4289-9030-c4eb528c6155/entry?categoryUuid=e8c110ad-64aa-4e8e-8a86-f2f152f6a942"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("entry.json")));
    stubFor(get(urlEqualTo("/event/31751038-df46-40f8-84ac-2e72ed604550/entry?categoryUuid=e8c110ad-64aa-4e8e-8a86-f2f152f6a942"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("entry-2014-arg-mgp.json")));
    stubFor(get(urlEqualTo("/event/20bb257f-b1ba-4289-9030-c4eb528c6155/entry?categoryUuid=549640b8-fd9c-4245-acfd-60e4bc38b25c"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("empty.json")));
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
    stubFor(get(urlEqualTo("/sessions?eventUuid=3e7c342a-e406-470f-956f-d685be970bf4&categoryUuid=e8c110ad-64aa-4e8e-8a86-f2f152f6a942"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("sessions-test-2022-my1-gp.json")));
    stubFor(get(urlEqualTo("/sessions?eventUuid=aacf14f8-fd7f-42d3-be6e-54add0eab84f&categoryUuid=e8c110ad-64aa-4e8e-8a86-f2f152f6a942"))
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
    stubFor(get(urlEqualTo("/session/b239e98b-9739-4fe3-ab6e-a6c3f5348226/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-qat-21-fp4-gp.json")));
    stubFor(get(urlEqualTo("/session/fae273c4-defb-4bac-84c8-e3283c5b088b/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications.json")));
    stubFor(get(urlEqualTo("/session/cfa834e4-91e8-458c-a12e-fc628dd071bf/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-rac.json")));
    stubFor(get(urlEqualTo("/session/64e9d65b-12cd-4436-8b63-549ac516bf02/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classification-fp2.json")));
    stubFor(get(urlEqualTo("/session/117144ae-2a0b-4d42-8d89-ab96253470d2/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-qat-21-fp1-gp.json")));
    stubFor(get(urlEqualTo("/session/253060b6-3562-446e-b259-f6ee49cc6714/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("empty.json")));
    stubFor(get(urlEqualTo("/session/917abd19-78e9-4ee8-9ea8-eafcf3912397/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("empty.json")));
    stubFor(get(urlEqualTo("/session/f638831c-aa9c-434d-b960-aa9f1aa310f5/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("empty.json")));
    stubFor(get(urlEqualTo("/session/f3d521b7-7c0b-474b-98c7-4013ae63e39c/classification?test=false"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("empty.json")));
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
    stubFor(get(urlEqualTo("/files/results/2014/ARG/MotoGP/FP1/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Classification-2014-arg-mgp-fp1.pdf")));
    stubFor(get(urlEqualTo("/files/results/2014/ARG/MotoGP/RAC/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Classification-2014-arg-mgp-rac.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP4/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/empty.pdf")));
  }


  private void stubTestClassifications() {
    stubFor(get(urlEqualTo("/session/7aed8f0a-10b4-4a0e-9ef8-964f34687718/classification?test=true"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-test-je1-gp-fp2.json")));
    stubFor(get(urlEqualTo("/session/baaef7a9-8f8c-4f5c-9e2d-40192824e66b/classification?test=true"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("classifications-test-je1-gp-fp1.json")));
    stubFor(get(urlEqualTo("/session/b272fd1a-53f2-449b-b51b-ec222de9f9ed/classification?test=true"))
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
  }

  private void stubEvents() {
    stubFor(get(urlEqualTo("/events?seasonUuid=db8dc197-c7b2-4c1b-b3a4-7dc723c087ed"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("events.json")));
    stubFor(get(urlEqualTo("/events?seasonUuid=db8dc197-c7b2-4c1b-b3a4-6dc534c014ef"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("events-2022.json")));
    stubFor(get(urlEqualTo("/events?seasonUuid=bf5c15b5-f062-4a88-a304-29892743108d"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("events-2014.json")));
  }

  private void stubCategories() {
    stubFor(get(urlEqualTo("/categories?eventUuid=20bb257f-b1ba-4289-9030-c4eb528c6155"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("categories.json")));
    stubFor(get(urlEqualTo("/categories?eventUuid=0d106a9a-95c9-4e13-9084-90c78c149f4a"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("category-ita.json")));
    stubFor(get(urlEqualTo("/categories?eventUuid=aacf14f8-fd7f-42d3-be6e-54add0eab84f"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("category-JE1-2022.json")));
    stubFor(get(urlEqualTo("/categories?eventUuid=3e7c342a-e406-470f-956f-d685be970bf4"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("category-MY1-2022.json")));
    stubFor(get(urlEqualTo("/categories?eventUuid=31751038-df46-40f8-84ac-2e72ed604550"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("category-ARG-2014.json")));
  }

  @Override
  public void stop() {
    if (null != wireMockServer) {
      wireMockServer.stop();
    }
  }
}