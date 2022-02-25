package org.teknichrono.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.teknichrono.model.client.Season;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@RegisterRestClient(configKey = "results-api")
public interface ResultsService {

  @GET
  @Path("/seasons")
  List<Season> getTestSeasons(@QueryParam boolean test);

  @GET
  @Path("/seasons")
  List<Season> getSeasons();
}