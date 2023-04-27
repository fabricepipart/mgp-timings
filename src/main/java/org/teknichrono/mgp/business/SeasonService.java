package org.teknichrono.mgp.business;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.api.client.ResultsClient;
import org.teknichrono.mgp.api.model.result.Season;
import org.teknichrono.mgp.util.CsvConverterFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class SeasonService {

  @Inject
  @RestClient
  ResultsClient resultsClient;

  @Inject
  CsvConverterFactory csvConverterFactory;

  public List<Season> getSeasons() {
    return resultsClient.getSeasons();
  }

  public Season getSeason(int year) {
    return resultsClient.getSeasons().stream().filter(s -> s.year == year).findFirst().get();
  }

  public String getSeasonsAsCsv() throws IOException {
    return csvConverterFactory.getSeasonCsvConverter().convertToCsv(resultsClient.getSeasons(), Season.class);
  }

  public List<Season> getTestSeasons() {
    return resultsClient.getTestSeasons(true);
  }

  public Season getCurrentSeason() {
    return resultsClient.getSeasons().stream().filter(s -> s.current).findAny().get();
  }

  public Season getCurrentTestSeason() {
    return resultsClient.getTestSeasons(true).stream().filter(s -> s.current).findAny().get();
  }
}
