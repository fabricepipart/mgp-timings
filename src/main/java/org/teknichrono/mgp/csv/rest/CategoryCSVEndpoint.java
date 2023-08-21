package org.teknichrono.mgp.csv.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.teknichrono.mgp.business.service.CategoryService;
import org.teknichrono.mgp.business.service.EventService;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Event;

import java.util.List;
import java.util.Optional;

@Path("/csv")
public class CategoryCSVEndpoint extends CSVEndpoint {

  @Inject
  EventService eventService;

  @Inject
  CategoryService categoryService;

  @GET
  @Produces(CSV_MEDIA_TYPE)
  @Transactional
  @Path("/{year}/{eventShortName}")
  public Response getEventCategories(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName) {
    Optional<Event> event = eventService.getEvent(year, eventShortName);
    if (event.isEmpty()) {
      throw new NotFoundException();
    }
    List<Category> categories = categoryService.categoriesOfEvent(event.get());
    String filename = String.format("categories-%d-%s.csv", year, eventShortName);
    return csvOutput(categories, Category.class, filename);
  }

}
