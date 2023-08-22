package org.teknichrono.mgp.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.client.rest.ResultsClient;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SeasonService {

  @Inject
  @RestClient
  ResultsClient resultsClient;

  public List<Season> getSeasons() {
    return resultsClient.getSeasons();
  }

  public Optional<Season> getSeason(int year) {
    return resultsClient.getSeasons().stream().filter(s -> s.year == year).findFirst();
  }

  public Optional<Season> getCurrentSeason() {
    return resultsClient.getSeasons().stream().filter(s -> s.current).findAny();
  }
}
