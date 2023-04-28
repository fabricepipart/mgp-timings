package org.teknichrono.mgp.client.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Classification;
import org.teknichrono.mgp.client.model.result.EntryList;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.client.model.result.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@RegisterRestClient(configKey = "results-api")
public interface ResultsClient {

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
  @Path("/season/{season}/events")
  List<Event> getTestEventsOfSeason(@PathParam("season") String season, @QueryParam("test") boolean test);

  @GET
  @Path("/season/{season}/events")
  List<Event> getFinishedEventsOfSeason(@PathParam("season") String season, @QueryParam("finished") boolean finished);

  @GET
  @Path("/event/{event}")
  Event getEvent(@PathParam("event") String event);

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
  Classification getClassification(@PathParam("session") String session);

  @GET
  @Path("/session/{session}/test-classifications")
  Classification getTestClassification(@PathParam("session") String session);


// $seasons_url = "http://localhost:8089/api/results-front/be/results-api/seasons?test=1";
// $events_url = "http://localhost:8089/api/results-front/be/results-api/season/$season/events?finished=1";
// $categories_url = "http://localhost:8089/api/results-front/be/results-api/event/$event/categories";
// $sessions_url = "http://localhost:8089/api/results-front/be/results-api/event/$event/category/category/$category/sessions";
// $results_url = "http://localhost:8089/api/results-front/be/results-api/session/$sesion/classifications"
// $results_url = "http://localhost:8089/api/results-front/be/results-api/session/$sesion/test-classifications"
}