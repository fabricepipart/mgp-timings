package org.teknichrono.mgp.business;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.api.client.ResultsClient;
import org.teknichrono.mgp.api.model.result.Category;
import org.teknichrono.mgp.api.model.result.Event;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryService {

  @Inject
  @RestClient
  ResultsClient resultsClient;

  @Inject
  EventService eventService;

  public List<Category> categoriesOfEvent(int year, String eventShortName) {
    Optional<Event> event = eventService.getEventOrTestOfYear(year, eventShortName);
    if (event.isPresent()) {
      return resultsClient.getCategoriesOfEvent(event.get().id);
    }
    return null;
  }

  public List<Category> categoriesOfEvent(Event event) {
    return resultsClient.getCategoriesOfEvent(event.id);
  }

  public Optional<Category> categoryOfEvent(int year, String eventShortName, String category) {
    return categoriesOfEvent(year, eventShortName).stream()
        .filter(c -> c.name.toLowerCase().contains(category.toLowerCase()))
        .findFirst();
  }
}
