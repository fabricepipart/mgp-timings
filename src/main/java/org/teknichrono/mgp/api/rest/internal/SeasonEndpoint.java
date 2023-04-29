package org.teknichrono.mgp.api.rest.internal;

import org.teknichrono.mgp.business.service.SeasonService;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.csv.util.CsvConverterFactory;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/internal/season")
public class SeasonEndpoint {

  @Inject
  SeasonService seasonService;

  @Inject
  CsvConverterFactory csvConverterFactory;

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
      String csvResults = csvConverterFactory.getSeasonCsvConverter().convertToCsv(seasonService.getSeasons(), Season.class);
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

