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
import org.teknichrono.mgp.api.model.EventOutput;
import org.teknichrono.mgp.business.service.CategoryService;
import org.teknichrono.mgp.business.service.EventService;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Event;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("")
public class EventEndpoint {

  @Inject
  EventService eventService;

  @Inject
  CategoryService categoryService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}")
  public EventOutput getEventCategories(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName) {
    Optional<Event> event = eventService.getEvent(year, eventShortName);
    if (event.isEmpty()) {
      throw new NotFoundException();
    }
    EventOutput toReturn = EventOutput.from(event.get());
    List<Category> categories = categoryService.categoriesOfEvent(event.get());
    List<Choice> categoriesNames = categories.stream().map(c -> Choice.from(c.getShortName(), c.name)).collect(Collectors.toList());
    toReturn.categories.addAll(categoriesNames);
    return toReturn;
  }


}
