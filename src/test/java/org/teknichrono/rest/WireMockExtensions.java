package org.teknichrono.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
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

    //stubFor(get(urlMatching(".*")).atPriority(10).willReturn(aResponse().proxiedFrom("https://www.motogp.com/api/results-front/be/results-api")));

    return Collections.singletonMap("quarkus.rest-client.results-api.url", wireMockServer.baseUrl());
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

  @Override
  public void stop() {
    if (null != wireMockServer) {
      wireMockServer.stop();
    }
  }
}