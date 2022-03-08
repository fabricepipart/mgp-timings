package org.teknichrono.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.teknichrono.client.ResultsService;
import org.teknichrono.model.client.Session;
import org.teknichrono.model.client.SessionClassification;
import org.teknichrono.util.ClassificationCsvConverter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/classification")
public class ClassificationEndpoint {


  private static final Logger LOGGER = Logger.getLogger(ClassificationEndpoint.class);

  @Inject
  @RestClient
  ResultsService resultsService;

  @Inject
  SessionEndpoint sessionEndpoint;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}")
  public SessionClassification getClassifications(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String session) {
    List<Session> sessions = sessionEndpoint.getSessions(year, eventShortName, category);
    Session sessionMatch = sessions.stream().filter(s -> getSessionName(s).equalsIgnoreCase(session)).findFirst().get();
    return resultsService.getClassification(sessionMatch.id);
  }

  private String getSessionName(Session s) {
    if (s.number != null) {
      return s.type + s.number;
    }
    return s.type;
  }


  @GET
  @Path("/{year}/{eventShortName}/{category}/{session}/csv")
  @Produces("text/csv")
  @Transactional
  public Response listAllToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String session) {
    try {
      ClassificationCsvConverter csvConverter = new ClassificationCsvConverter();
      String csvResults = csvConverter.convertToCsv(this.getClassifications(year, eventShortName, category, session).classification);
      return Response.ok().entity(csvResults).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }

}
