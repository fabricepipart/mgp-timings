package org.teknichrono.mgp.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.teknichrono.mgp.model.result.Category;
import org.teknichrono.mgp.model.result.EntryList;
import org.teknichrono.mgp.model.result.Event;
import org.teknichrono.mgp.model.result.Season;
import org.teknichrono.mgp.model.result.Session;
import org.teknichrono.mgp.model.result.SessionClassification;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@RegisterRestClient(configKey = "results-api")
public interface ResultsService {

  @GET
  @Path("/seasons")
  List<Season> getTestSeasons(@QueryParam("test") boolean test);

  @GET
  @Path("/seasons")
  List<Season> getSeasons();


  @GET
  @Path("/season/{season}/events")
  List<Event> getEventsOfSeason(@PathParam("season") String season);

  @GET
  @Path("/season/db8dc197-c7b2-4c1b-b3a4-7dc723c087ed/events")
  List<Event> getEventsTest();

  @GET
  @Path("/season/{season}/events")
  List<Event> getFinishedEventsOfSeason(@PathParam("season") String season, @QueryParam("finished") boolean finished);

  @GET
  @Path("/event/{event}/categories")
  List<Category> getCategoriesOfEvent(@PathParam("event") String event);

  @GET
  @Path("/event/{event}/category/{category}/sessions")
  List<Session> getSessions(@PathParam("event") String event, @PathParam("category") String category);

  @GET
  @Path("/event/{event}/category/{category}/entry")
  EntryList getEntries(@PathParam("event") String event, @PathParam("category") String category);

  @GET
  @Path("/session/{session}/classifications")
  SessionClassification getClassification(@PathParam("session") String session);

// $seasons_url = "https://www.motogp.com/api/results-front/be/results-api/seasons?test=1";
// $events_url = "https://www.motogp.com/api/results-front/be/results-api/season/$season/events?finished=1";
// $categories_url = "https://www.motogp.com/api/results-front/be/results-api/event/$event/categories";
// $sessions_url = "https://www.motogp.com/api/results-front/be/results-api/event/$event/category/category/$category/sessions";
// $results_url = "https://www.motogp.com/api/results-front/be/results-api/session/$sesion/classifications"
}