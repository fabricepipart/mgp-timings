package org.teknichrono.mgp.csv.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.teknichrono.mgp.api.model.CategoryOutput;
import org.teknichrono.mgp.api.model.SessionRider;
import org.teknichrono.mgp.business.service.CategoryService;
import org.teknichrono.mgp.business.service.RiderService;
import org.teknichrono.mgp.client.model.result.Category;

import java.util.List;
import java.util.Optional;

@Path("/csv")
public class RiderCSVEndpoint extends CSVEndpoint {

  @Inject
  CategoryService categoryService;

  @Inject
  RiderService riderService;

  @GET
  @Produces(CSV_MEDIA_TYPE)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/" + CategoryOutput.RIDERS)
  public Response getEventRiders(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName) {
    Optional<Category> category = categoryService.categoryOfEvent(year, eventShortName, categoryName);
    if (category.isEmpty()) {
      throw new NotFoundException();
    }
    Optional<List<SessionRider>> riders = riderService.getRidersOfEvent(year, eventShortName, categoryName);
    if (riders.isEmpty()) {
      throw new NotFoundException(String.format("Could not find riders of category %s during event %s in %d", categoryName, eventShortName, year));
    }
    String filename = String.format("riders-%d-%s-%s.csv", year, eventShortName, categoryName);
    return csvOutput(riders.get(), SessionRider.class, filename);
  }
}
