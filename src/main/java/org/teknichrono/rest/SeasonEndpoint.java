package org.teknichrono.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.client.ResultsService;
import org.teknichrono.model.client.Season;
import org.teknichrono.util.CsvConverter;

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
  ResultsService resultsService;

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
      CsvConverter csvConverter = new CsvConverter();
      String csvResults = csvConverter.convertToCsv(resultsService.getSeasons());
      return Response.ok().entity(csvResults).build();
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

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/hello")
  public String hello() {
    return "Hello MotoGP";
  }
}

