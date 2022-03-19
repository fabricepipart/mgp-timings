package org.teknichrono.mgp.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.client.ResultsService;
import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.result.Session;
import org.teknichrono.mgp.model.result.SessionClassification;
import org.teknichrono.mgp.util.CsvConverter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/classification")
public class ClassificationEndpoint {


  private static final Logger LOGGER = Logger.getLogger(ClassificationEndpoint.class);

  @Inject
  @RestClient
  ResultsService resultsService;

  @Inject
  SessionEndpoint sessionEndpoint;

  @Inject
  CsvConverter<Classification> csvConverter;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}")
  public SessionClassification getClassifications(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String session) {
    Session sessionMatch = sessionEndpoint.getSessionByName(year, eventShortName, category, session);
    return resultsService.getClassification(sessionMatch.id);
  }


  @GET
  @Path("/{year}/{eventShortName}/{category}/{session}/csv")
  @Produces("text/csv")
  @Transactional
  public Response listAllToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String session) {
    try {
      String csvResults = csvConverter.convertToCsv(this.getClassifications(year, eventShortName, category, session).classification);
      return Response.ok().entity(csvResults).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }

}
