package org.teknichrono.mgp.client.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.EntryList;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.client.model.result.SessionResults;

import java.util.List;

/**
 * Used to be
 * <p>
 * seasons_url = "http://localhost:8089/api/results-front/be/results-api/seasons?test=1";
 * events_url = "http://localhost:8089/api/results-front/be/results-api/season/$season/events?finished=1";
 * categories_url = "http://localhost:8089/api/results-front/be/results-api/event/$event/categories";
 * sessions_url = "http://localhost:8089/api/results-front/be/results-api/event/$event/category/category/$category/sessions";
 * results_url = "http://localhost:8089/api/results-front/be/results-api/session/$sesion/classifications"
 * results_url = "http://localhost:8089/api/results-front/be/results-api/session/$sesion/test-classifications"
 */
@RegisterRestClient(configKey = "results-api")
public interface ResultsClient {

  @GET
  @Path("/seasons")
  List<Season> getSeasons();

  @GET
  @Path("/events")
  List<Event> getEventsOfSeason(@QueryParam("seasonUuid") String season);

  @GET
  @Path("/events")
  List<Event> getFinishedEventsOfSeason(@QueryParam("seasonUuid") String season, @QueryParam("isFinished") boolean finished);

  @GET
  @Path("/events")
  Event getEvent(@QueryParam("eventUuid") String event);

  @GET
  @Path("/categories")
  List<Category> getCategoriesOfEvent(@QueryParam("eventUuid") String event);

  @GET
  @Path("/sessions")
  List<Session> getSessions(@QueryParam("eventUuid") String event, @QueryParam("categoryUuid") String category);

  @GET
  @Path("/event/{eventUuid}/entry")
  EntryList getEntries(@PathParam("eventUuid") String event, @QueryParam("categoryUuid") String category);

  @GET
  @Path("/session/{sessionUuid}/classification")
  SessionResults getClassification(@PathParam("sessionUuid") String session, @QueryParam("test") boolean test);

}