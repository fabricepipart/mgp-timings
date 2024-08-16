package org.teknichrono.mgp.api.rest;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.api.model.SessionResultOutput;
import org.teknichrono.mgp.business.parser.PdfParsingException;

@Path("")
public class SessionResultsEndpoint extends SessionEndpoint {

  private static final Logger LOGGER = Logger.getLogger(SessionResultsEndpoint.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{shortSessionName}")
  public SessionResultOutput getSessionResult(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName, @PathParam("shortSessionName") String shortSessionName) {
    try {
      return sessionService.getResultDetails(year, eventShortName, categoryName, shortSessionName);
    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the details PDF for session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      LOGGER.error(message, e);
      throw new InternalServerErrorException(message, e);
    } catch (ClientErrorException cee) {
      String message = String.format("Error when looking for the results of session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      LOGGER.error(message, cee);
      throw cee;
    }
  }
}
