package org.teknichrono.mgp.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.client.ResultsClient;
import org.teknichrono.mgp.model.result.Season;
import org.teknichrono.mgp.util.CsvConverter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/season")
public class SeasonEndpoint {

  @Inject
  @RestClient
  ResultsClient resultsService;

  @Inject
  CsvConverter<Season> csvConverter;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public List<Season> listAll() {
    return resultsService.getSeasons();
  }


  @GET
  @Path("/csv")
  @Produces("text/csv")
  @Transactional
  public Response listAllToCsv() {
    try {
      String csvResults = csvConverter.convertToCsv(resultsService.getSeasons(), Season.class);
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
    return resultsService.getTestSeasons(true);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/current")
  public Season current() {
    return resultsService.getSeasons().stream().filter(s -> s.current).findAny().get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/current/test")
  public Season currentTest() {
    return resultsService.getTestSeasons(true).stream().filter(s -> s.current).findAny().get();
  }

}

