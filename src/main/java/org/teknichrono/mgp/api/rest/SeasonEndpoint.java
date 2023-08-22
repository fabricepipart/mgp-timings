package org.teknichrono.mgp.api.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.teknichrono.mgp.api.model.Choice;
import org.teknichrono.mgp.api.model.SeasonOutput;
import org.teknichrono.mgp.business.service.EventService;
import org.teknichrono.mgp.business.service.SeasonService;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.model.result.Season;

import java.util.List;
import java.util.Optional;
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
    Optional<Season> seasonOptional = seasonService.getSeason(year);
    if (seasonOptional.isEmpty()) {
      throw new NotFoundException("Could not find the season " + year);
    }
    SeasonOutput toReturn = SeasonOutput.from(seasonOptional.get());
    List<Choice> racesNames = eventService.getRaces(year).stream()
        .map(e -> Choice.from(e.short_name, buildName(e)))
        .collect(Collectors.toList());
    toReturn.races.addAll(racesNames);
    List<Choice> testsNames = eventService.getTests(year).stream()
        .map(e -> Choice.from(e.short_name, buildName(e)))
        .collect(Collectors.toList());
    toReturn.tests.addAll(testsNames);
    return toReturn;
  }

  private String buildName(Event e) {
    return String.format("%s (%s)", e.name, e.circuit.name);
  }

}
