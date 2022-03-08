package org.teknichrono.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.teknichrono.client.ResultsService;
import org.teknichrono.model.client.Category;
import org.teknichrono.model.client.Event;
import org.teknichrono.model.client.Session;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/session")
public class SessionEndpoint {


  private static final Logger LOGGER = Logger.getLogger(SessionEndpoint.class);

  @Inject
  @RestClient
  ResultsService resultsService;

  @Inject
  EventEndpoint eventEndpoint;

  @Inject
  CategoryEndpoint categoryEndpoint;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}")
  public List<Session> getSessions(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    Event event = eventEndpoint.eventsOfYear(year)
        .stream().filter(e -> eventShortName.equalsIgnoreCase(e.short_name)).findFirst().get();
    Category cat = categoryEndpoint.categoriesOfEvent(year, eventShortName)
        .stream().filter(c -> c.name.toLowerCase().contains(category.toLowerCase())).findFirst().get();
    return resultsService.getSessions(event.id, cat.id);
  }

}
