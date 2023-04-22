package org.teknichrono.mgp.api.rest;

import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.business.service.EventService;
import org.teknichrono.mgp.business.service.SeasonService;
import org.teknichrono.mgp.api.model.Choice;
import org.teknichrono.mgp.api.model.SeasonOutput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("")
public class SeasonEndpoint {

  @Inject
  SeasonService seasonService;

  @Inject
  EventService eventService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}")
  public SeasonOutput getYearEvents(@PathParam("year") int year) {
    Season season = seasonService.getSeason(year);
    SeasonOutput toReturn = SeasonOutput.from(season);
    List<Choice> racesNames = eventService.getEventsOfYear(year).stream().map(e -> Choice.from(e.short_name, e.sponsored_name)).collect(Collectors.toList());
    toReturn.races.addAll(racesNames);
    List<Choice> testsNames = eventService.getTestsOfYear(year).stream().map(e -> Choice.from(e.short_name, e.sponsored_name)).collect(Collectors.toList());
    toReturn.tests.addAll(testsNames);
    return toReturn;
  }

}
