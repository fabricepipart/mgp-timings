package org.teknichrono.mgp.it;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    stubClassifications();

    stubRiders();
    //stubFor(get(urlMatching(".*")).atPriority(10).willReturn(aResponse().proxiedFrom("https://www.motogp.com/api/results-front/be/results-api")));

    Map<String, String> config = new HashMap<>();
    config.put("quarkus.rest-client.results-api.url", wireMockServer.baseUrl());
    config.put("quarkus.rest-client.riders-api.url", wireMockServer.baseUrl());

    return config;
  }

  private void stubRiders() {
    List<Integer> ridersLegacyId = Arrays.asList(158, 6267, 6854, 6976, 7086, 7199, 7236, 7246, 7409, 7444, 7646, 7741, 8049, 8141, 8146, 8148, 8150, 8173, 8273, 8295, 8431, 8520, 8947);
    for (Integer riderLegacyId : ridersLegacyId) {
      stubFor(get(urlEqualTo("/riders/" + riderLegacyId))
          .willReturn(aResponse()
              .withHeader("Content-Type", "application/json")
              .withBodyFile("rider/" + riderLegacyId + ".json")));
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

  private void stubClassifications() {
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
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/RAC/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Classification-rac.pdf")));
    stubFor(get(urlEqualTo("/files/results/2021/QAT/MotoGP/FP1/Classification.pdf"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/pdf")
            .withBodyFile("pdf/Classification-fp1.pdf")));


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
  }

  @Override
  public void stop() {
    if (null != wireMockServer) {
      wireMockServer.stop();
    }
  }
}