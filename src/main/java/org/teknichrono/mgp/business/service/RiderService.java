package org.teknichrono.mgp.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.api.model.SessionRider;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Entry;
import org.teknichrono.mgp.client.model.result.EntryList;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.model.rider.RiderDetails;
import org.teknichrono.mgp.client.rest.ResultsClient;
import org.teknichrono.mgp.client.rest.RidersClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RiderService {

  @Inject
  @RestClient
  RidersClient riderClient;

  @Inject
  @RestClient
  ResultsClient resultsClient;

  @Inject
  EventService eventService;

  @Inject
  CategoryService categoryService;

  public RiderDetails getRider(Integer legacyId) {
    return riderClient.getRider(legacyId);
  }

  public Optional<List<Entry>> getEntries(int year, String eventShortName, String category) {
    Optional<Event> event = eventService.getEvent(year, eventShortName);
    Optional<Category> cat = categoryService.categoryOfEvent(year, eventShortName, category);
    if (event.isEmpty() || cat.isEmpty()) {
      return Optional.empty();
    }
    EntryList entries = resultsClient.getEntries(event.get().id, cat.get().id);
    if (entries == null || entries.entry == null) {
      return Optional.empty();
    }
    return Optional.of(entries.entry);
  }

  public Optional<List<SessionRider>> getRidersOfEvent(int year, String eventShortName, String category) {
    Optional<List<Entry>> entries = getEntries(year, eventShortName, category);
    if (entries.isEmpty()) {
      return Optional.empty();
    }
    List<SessionRider> riders = new ArrayList<>();
    for (Entry e : entries.get()) {
      SessionRider rider = new SessionRider();
      rider.fill(e, getRider(e.rider.legacy_id));
      riders.add(rider);
    }
    return Optional.of(riders);
  }

}
