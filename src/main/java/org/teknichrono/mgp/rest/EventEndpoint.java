package org.teknichrono.mgp.rest;

import org.teknichrono.mgp.api.model.result.Category;
import org.teknichrono.mgp.api.model.result.Event;
import org.teknichrono.mgp.business.CategoryService;
import org.teknichrono.mgp.business.EventService;
import org.teknichrono.mgp.model.output.Choice;
import org.teknichrono.mgp.model.output.EventOutput;

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
    Optional<Event> event = eventService.getEventOrTestOfYear(year, eventShortName);
    if (event.isEmpty()) {
      throw new NotFoundException();
    }
    EventOutput toReturn = EventOutput.from(event.get());
    List<Category> categories = categoryService.categoriesOfEvent(event.get());
    List<Choice> categoriesNames = categories.stream().map(c -> Choice.from(c.name.replaceAll("[^A-Za-z0-9]", "").toUpperCase(), c.name)).collect(Collectors.toList());
    toReturn.categories.addAll(categoriesNames);
    return toReturn;
  }

}
