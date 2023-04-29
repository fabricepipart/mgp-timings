package org.teknichrono.mgp.api.rest.internal;

import org.teknichrono.mgp.business.service.EventService;
import org.teknichrono.mgp.client.model.result.Event;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/internal/event")
public class InternalEventEndpoint {

  @Inject
  EventService eventService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}")
  public List<Event> eventsOfYear(@PathParam("year") int year) {
    return eventService.getEventsOfYear(year);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/test/{year}")
  public List<Event> testsOfYear(@PathParam("year") int year) {
    return eventService.getTestsOfYear(year);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}")
  public Event eventOfYear(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName) {
    Optional<Event> eventOfYear = eventService.getEventOfYear(year, eventShortName);
    if (eventOfYear.isEmpty()) {
      throw new NotFoundException();
    }
    return eventOfYear.get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/test/{year}/{eventShortName}")
  public Event testOfYear(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName) {
    Optional<Event> testOfYear = eventService.getTestOfYear(year, eventShortName);
    if (testOfYear.isEmpty()) {
      throw new NotFoundException();
    }
    return testOfYear.get();
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/names")
  public List<String> eventsNamesOfYear(@PathParam("year") int year) {
    return eventsOfYear(year).stream().map(e -> e.short_name).collect(Collectors.toList());
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/test/{year}/names")
  public List<String> testsNamesOfYear(@PathParam("year") int year) {
    return testsOfYear(year).stream().map(e -> e.short_name).collect(Collectors.toList());

  }

}
