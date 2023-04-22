package org.teknichrono.mgp.api.rest;

import org.teknichrono.mgp.api.model.CategoryOutput;
import org.teknichrono.mgp.api.model.CategoryRidersOutput;
import org.teknichrono.mgp.api.model.Choice;
import org.teknichrono.mgp.api.model.SessionRider;
import org.teknichrono.mgp.business.service.CategoryService;
import org.teknichrono.mgp.business.service.RiderService;
import org.teknichrono.mgp.business.service.SessionService;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Session;

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

@Path("")
public class CategoryEndpoint {

  @Inject
  SessionService sessionService;

  @Inject
  CategoryService categoryService;

  @Inject
  RiderService riderService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}")
  public CategoryOutput getEventCategorySessions(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName) {
    Optional<Category> category = categoryService.categoryOfEvent(year, eventShortName, categoryName);
    if (category.isEmpty()) {
      throw new NotFoundException();
    }
    CategoryOutput toReturn = CategoryOutput.from(category.get());
    List<Session> sessions = sessionService.getSessions(year, eventShortName, categoryName);
    List<Choice> sessionsNames = sessions.stream().map(s -> Choice.from(s.getSessionName(), null)).collect(Collectors.toList());
    toReturn.sessions.addAll(sessionsNames);
    return toReturn;
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/" + CategoryOutput.RIDERS)
  public CategoryRidersOutput getEventRiders(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName) {
    Optional<Category> category = categoryService.categoryOfEvent(year, eventShortName, categoryName);
    if (category.isEmpty()) {
      throw new NotFoundException();
    }
    Optional<List<SessionRider>> riders = riderService.getRidersOfEvent(year, eventShortName, categoryName);
    if (riders.isEmpty()) {
      throw new NotFoundException(String.format("Could not find riders of category %s during event %s in %d", categoryName, eventShortName, year));
    }
    return CategoryRidersOutput.from(category.get(), riders.get());
  }

}
