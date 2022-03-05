package org.teknichrono.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.teknichrono.model.client.Event;
import org.teknichrono.model.client.Season;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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


// $seasons_url = "https://www.motogp.com/api/results-front/be/results-api/seasons?test=1";
// $events_url = "https://www.motogp.com/api/results-front/be/results-api/season/$season/events?finished=1";
// $categories_url = "https://www.motogp.com/api/results-front/be/results-api/event/$event/categories";
// $sessions_url = "https://www.motogp.com/api/results-front/be/results-api/event/$event/category/category/$category/sessions";
// $results_url = "https://www.motogp.com/api/results-front/be/results-api/session/$sesion/classifications"
}