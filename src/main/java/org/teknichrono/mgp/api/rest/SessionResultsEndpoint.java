package org.teknichrono.mgp.api.rest;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.api.model.SessionResultOutput;
import org.teknichrono.mgp.api.rest.internal.InternalSessionEndpoint;
import org.teknichrono.mgp.business.parser.PdfParsingException;
import org.teknichrono.mgp.client.model.result.Classification;
import org.teknichrono.mgp.client.model.result.Session;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("")
public class SessionResultsEndpoint extends SessionEndpoint {

  private static final Logger LOGGER = Logger.getLogger(InternalSessionEndpoint.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{shortSessionName}")
  public SessionResultOutput getSessionResult(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName, @PathParam("shortSessionName") String shortSessionName) {
    try {
      Optional<Classification> resultsOptional = sessionService.getResults(year, eventShortName, categoryName, shortSessionName);
      if (resultsOptional.isEmpty()) {
        throw new NotFoundException(String.format("Could not find results of session %s of category %s of %s in %d", shortSessionName, categoryName, eventShortName, year));
      }
      Session session = getSession(year, eventShortName, categoryName, shortSessionName);
      Classification results = resultsOptional.get();
      List<SessionClassificationOutput> resultDetails = sessionService.getResultDetails(year, eventShortName, categoryName, shortSessionName);
      return SessionResultOutput.from(session, results, resultDetails);
    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the details PDF for session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      LOGGER.error(message, e);
      throw new InternalServerErrorException(message, e);
    }
  }
}
