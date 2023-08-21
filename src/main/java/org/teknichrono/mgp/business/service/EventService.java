package org.teknichrono.mgp.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.client.rest.ResultsClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class EventService {

  @Inject
  @RestClient
  ResultsClient resultsClient;

  @Inject
  SeasonService seasonService;

  public List<Event> getEvents(int year) {
    Optional<Season> season = seasonService.getSeasons().stream().filter(s -> s.year.intValue() == year).findFirst();
    if (season.isPresent()) {
      return resultsClient.getEventsOfSeason(season.get().id);
    }
    return Collections.emptyList();
  }

  public List<Event> getRaces(int year) {
    return getEvents(year).stream().filter(e -> !e.test).collect(Collectors.toList());
  }

  public List<Event> getTests(int year) {
    return getEvents(year).stream().filter(e -> e.test).collect(Collectors.toList());
  }

  public Optional<Event> getEvent(int year, String eventShortName) {
    return getEvents(year).stream().filter(e -> eventShortName.equalsIgnoreCase(e.short_name)).findFirst();
  }

  public Optional<Event> getRace(int year, String eventShortName) {
    return getRaces(year).stream().filter(e -> eventShortName.equalsIgnoreCase(e.short_name)).findFirst();
  }

  public Optional<Event> getTest(int year, String eventShortName) {
    return getTests(year).stream().filter(e -> eventShortName.equalsIgnoreCase(e.short_name)).findFirst();
  }
  
}
