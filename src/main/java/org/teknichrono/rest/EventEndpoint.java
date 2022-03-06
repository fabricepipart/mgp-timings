package org.teknichrono.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.client.ResultsService;
import org.teknichrono.model.client.Event;
import org.teknichrono.model.client.Season;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/event")
public class EventEndpoint {

  @Inject
  @RestClient
  ResultsService resultsService;

  @Inject
  SeasonEndpoint seasonEndpoint;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}")
  public List<Event> eventsOfYear(@PathParam("year") int year) {
    Season season = seasonEndpoint.listAll().stream().filter(s -> s.year == year).findFirst().get();
    return resultsService.getEventsOfSeason(season.id);
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/names")
  public List<String> eventsNamesOfYear(@PathParam("year") int year) {
    Season season = seasonEndpoint.listAll().stream().filter(s -> s.year == year).findFirst().get();
    return resultsService.getEventsOfSeason(season.id).stream().map(e -> e.short_name).collect(Collectors.toList());
  }

}
