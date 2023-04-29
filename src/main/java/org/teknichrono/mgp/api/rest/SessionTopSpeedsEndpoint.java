package org.teknichrono.mgp.api.rest;

import org.teknichrono.mgp.api.model.MaxSpeed;
import org.teknichrono.mgp.api.model.SessionOutput;
import org.teknichrono.mgp.api.model.SessionTopSpeedsOutput;
import org.teknichrono.mgp.business.parser.PdfParsingException;
import org.teknichrono.mgp.client.model.result.Session;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("")
public class SessionTopSpeedsEndpoint extends SessionEndpoint {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{shortSessionName}/" + SessionOutput.TOPSPEED_OPTION)
  public SessionTopSpeedsOutput getSessionTopSpeeds(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName, @PathParam("shortSessionName") String shortSessionName) {
    try {
      Optional<List<MaxSpeed>> topSpeeds = sessionService.getTopSpeeds(year, eventShortName, categoryName, shortSessionName);
      if (topSpeeds.isEmpty()) {
        throw new NotFoundException(String.format("Could not find analysis of session %s of category %s of %s in %d", shortSessionName, categoryName, eventShortName, year));
      }
      Session session = getSession(year, eventShortName, categoryName, shortSessionName);
      return SessionTopSpeedsOutput.from(session, topSpeeds.get());

    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the analysis PDF for session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      throw new InternalServerErrorException(message, e);
    }
  }
}
