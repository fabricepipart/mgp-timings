package org.teknichrono.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.teknichrono.client.ResultsService;
import org.teknichrono.model.client.Category;
import org.teknichrono.model.client.Event;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/category")
public class CategoryEndpoint {


  private static final Logger LOGGER = Logger.getLogger(CategoryEndpoint.class);

  @Inject
  @RestClient
  ResultsService resultsService;

  @Inject
  EventEndpoint eventEndpoint;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}")
  public List<Category> categoriesOfEvent(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName) {
    Event event = eventEndpoint.eventsOfYear(year).stream().filter(e -> eventShortName.equalsIgnoreCase(e.short_name)).findFirst().get();
    return resultsService.getCategoriesOfEvent(event.id);
  }

}
