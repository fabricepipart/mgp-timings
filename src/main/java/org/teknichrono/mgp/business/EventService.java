package org.teknichrono.mgp.business;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.api.client.ResultsClient;
import org.teknichrono.mgp.api.model.result.Event;
import org.teknichrono.mgp.api.model.result.Season;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventService {

  @Inject
  @RestClient
  ResultsClient resultsClient;

  @Inject
  SeasonService seasonService;

  public List<Event> getEventsOfYear(int year) {
    Optional<Season> season = seasonService.getSeasons().stream().filter(s -> s.year.intValue() == year).findFirst();
    if (season.isPresent()) {
      return resultsClient.getEventsOfSeason(season.get().id);
    }
    return null;
  }

  public List<Event> getTestsOfYear(int year) {
    Optional<Season> season = seasonService.getTestSeasons().stream().filter(s -> s.year.intValue() == year).findFirst();
    if (season.isPresent()) {
      return resultsClient.getTestEventsOfSeason(season.get().id, true);
    }
    return null;
  }

  public Optional<Event> getEventOfYear(int year, String eventShortName) {
    return getEventsOfYear(year).stream().filter(e -> eventShortName.equalsIgnoreCase(e.short_name)).findFirst();
  }

  public Optional<Event> getTestOfYear(int year, String eventShortName) {
    return getTestsOfYear(year).stream().filter(e -> eventShortName.equalsIgnoreCase(e.short_name)).findFirst();
  }


  public Optional<Event> getEventOrTestOfYear(int year, String eventShortName) {
    Optional<Event> event = getEventOfYear(year, eventShortName);
    if (event.isEmpty()) {
      event = getTestOfYear(year, eventShortName);
    }
    return event;
  }
}
