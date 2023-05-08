package org.teknichrono.mgp.api.rest.internal;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.teknichrono.mgp.business.service.SeasonService;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.csv.converter.CsvConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Path("/internal/season")
public class InternalSeasonEndpoint {

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
      String csvResults = csvConverterFactory.getCsvConverter(Season.class).convertToCsv(seasonService.getSeasons());
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
    Optional<Season> seasonOptional = seasonService.getCurrentSeason();
    if (seasonOptional.isEmpty()) {
      throw new NotFoundException("Could not find the current season");
    }
    return seasonOptional.get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/current/test")
  public Season currentTest() {
    Optional<Season> seasonOptional = seasonService.getCurrentTestSeason();
    if (seasonOptional.isEmpty()) {
      throw new NotFoundException("Could not find the current test season");
    }
    return seasonOptional.get();
  }

}

