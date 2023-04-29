package org.teknichrono.mgp.api.rest;

import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.SessionAnalysisOutput;
import org.teknichrono.mgp.api.model.SessionOutput;
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
public class SessionAnalysisEndpoint extends SessionEndpoint {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{shortSessionName}/" + SessionOutput.ANALYSIS_OPTION)
  public SessionAnalysisOutput getSessionAnalysis(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName, @PathParam("shortSessionName") String shortSessionName) {
    try {
      Optional<List<LapAnalysis>> analysis = sessionService.getAnalysis(year, eventShortName, categoryName, shortSessionName);
      if (analysis.isEmpty()) {
        throw new NotFoundException(String.format("Could not find analysis of session %s of category %s of %s in %d", shortSessionName, categoryName, eventShortName, year));
      }
      Session session = getSession(year, eventShortName, categoryName, shortSessionName);
      return SessionAnalysisOutput.from(session, analysis.get());

    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the analysis PDF for session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      throw new InternalServerErrorException(message, e);
    }
  }
}
