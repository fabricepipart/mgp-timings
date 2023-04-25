package org.teknichrono.mgp.rest.internal;

import org.teknichrono.mgp.business.SeasonService;
import org.teknichrono.mgp.api.model.result.Season;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/internal/season")
public class SeasonEndpoint {

  @Inject
  SeasonService seasonService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public List<Season> listAll() {
    return seasonService.getSeasons();
  }


  @GET
  @Path("/csv")
  @Produces("text/csv")
  @Transactional
  public Response listAllToCsv() {
    try {
      String csvResults = seasonService.getSeasonsAsCsv();
      return Response.ok().entity(csvResults).header("Content-Disposition", "attachment;filename=seasons.csv").build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/test")
  public List<Season> tests() {
    return seasonService.getTestSeasons();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/current")
  public Season current() {
    return seasonService.getCurrentSeason();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/current/test")
  public Season currentTest() {
    return seasonService.getCurrentTestSeason();
  }

}

