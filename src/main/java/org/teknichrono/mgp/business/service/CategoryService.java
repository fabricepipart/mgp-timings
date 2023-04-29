package org.teknichrono.mgp.business.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.rest.ResultsClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Collections;
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
    return Collections.emptyList();
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
