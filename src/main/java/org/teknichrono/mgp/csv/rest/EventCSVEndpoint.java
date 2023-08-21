package org.teknichrono.mgp.csv.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.teknichrono.mgp.business.service.EventService;
import org.teknichrono.mgp.client.model.result.Event;

import java.util.List;

@Path("/csv")
public class EventCSVEndpoint extends CSVEndpoint {

  @Inject
  EventService eventService;

  @GET
  @Produces(CSV_MEDIA_TYPE)
  @Transactional
  @Path("/{year}")
  public Response getYearEvents(@PathParam("year") int year) {
    List<Event> events = eventService.getEvents(year);
    if (events.isEmpty()) {
      throw new NotFoundException("Could not find any event for year " + year);
    }
    String filename = String.format("events-%d.csv", year);
    return csvOutput(events, Event.class, filename);
  }

}
