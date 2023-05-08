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
import org.teknichrono.mgp.business.service.SessionService;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Session;

import java.util.List;
import java.util.Optional;

@Path("/csv")
public class SessionCSVEndpoint extends CSVEndpoint {

  @Inject
  SessionService sessionService;

  @Inject
  CategoryService categoryService;

  @GET
  @Produces(CSV_MEDIA_TYPE)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}")
  public Response getEventCategorySessions(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName) {
    Optional<Category> category = categoryService.categoryOfEvent(year, eventShortName, categoryName);
    if (category.isEmpty()) {
      throw new NotFoundException();
    }
    List<Session> sessions = sessionService.getSessions(year, eventShortName, categoryName);
    String filename = String.format("sessions-%d-%s-%s.csv", year, eventShortName, categoryName);
    return csvOutput(sessions, Session.class, filename);
  }

}
