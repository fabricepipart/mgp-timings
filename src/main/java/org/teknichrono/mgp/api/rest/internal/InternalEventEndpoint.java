package org.teknichrono.mgp.api.rest.internal;

import org.teknichrono.mgp.business.service.EventService;
import org.teknichrono.mgp.client.model.result.Event;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
