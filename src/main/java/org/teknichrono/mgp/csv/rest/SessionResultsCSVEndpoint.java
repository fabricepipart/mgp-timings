package org.teknichrono.mgp.csv.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.business.parser.PdfParsingException;
import org.teknichrono.mgp.business.service.SessionService;

import java.util.List;

@Path("/csv")
public class SessionResultsCSVEndpoint extends CSVEndpoint {

  private static final Logger LOGGER = Logger.getLogger(SessionResultsCSVEndpoint.class);

  @Inject
  SessionService sessionService;

  @GET
  @Produces(CSV_MEDIA_TYPE)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{shortSessionName}")
  public Response getSessionResult(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName, @PathParam("shortSessionName") String shortSessionName) {
    try {
      List<SessionClassificationOutput> resultDetails = sessionService.getResultDetails(year, eventShortName, categoryName, shortSessionName);
      String filename = String.format("sessions-classification-%d-%s-%s-%s.csv", year, eventShortName, categoryName, shortSessionName);
      return csvOutput(resultDetails, SessionClassificationOutput.class, filename);
    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the details PDF for session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      LOGGER.error(message, e);
      throw new InternalServerErrorException(message, e);
    }
  }
}
