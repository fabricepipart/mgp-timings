package org.teknichrono.mgp.api.rest;

import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.MaxSpeed;
import org.teknichrono.mgp.api.model.SessionAnalysisOutput;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.api.model.SessionOutput;
import org.teknichrono.mgp.api.model.SessionResultOutput;
import org.teknichrono.mgp.api.model.SessionTopSpeedsOutput;
import org.teknichrono.mgp.business.parser.PdfParsingException;
import org.teknichrono.mgp.business.service.SessionService;
import org.teknichrono.mgp.client.model.result.Classification;
import org.teknichrono.mgp.client.model.result.Session;

import javax.inject.Inject;
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
public class SessionEndpoint {


  @Inject
  SessionService sessionService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{shortSessionName}")
  public SessionResultOutput getSessionResult(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName, @PathParam("shortSessionName") String shortSessionName) {
    Session session = getSession(year, eventShortName, categoryName, shortSessionName);
    try {
      Optional<Classification> resultsOptional = sessionService.getResults(year, eventShortName, categoryName, shortSessionName);
      if (resultsOptional.isEmpty()) {
        throw new NotFoundException(String.format("Could not find results of session %s of category %s of %s in %d", shortSessionName, categoryName, eventShortName, year));
      }
      Classification results = resultsOptional.get();
      List<SessionClassificationOutput> resultDetails = sessionService.getResultDetails(year, eventShortName, categoryName, shortSessionName);
      return SessionResultOutput.from(session, results, resultDetails);
    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the details PDF for session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      throw new InternalServerErrorException(message, e);
    }
  }

  private Session getSession(int year, String eventShortName, String categoryName, String shortSessionName) {
    Optional<Session> sessionOptional = sessionService.getSessionByName(year, eventShortName, categoryName, shortSessionName);
    if (sessionOptional.isEmpty()) {
      throw new NotFoundException(String.format("Could not find session %s of category %s of %s in %d", shortSessionName, categoryName, eventShortName, year));
    }
    return sessionOptional.get();
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{shortSessionName}/" + SessionOutput.ANALYSIS)
  public SessionAnalysisOutput getSessionAnalysis(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName, @PathParam("shortSessionName") String shortSessionName) {
    Session session = getSession(year, eventShortName, categoryName, shortSessionName);
    try {
      Optional<List<LapAnalysis>> analysis = sessionService.getAnalysis(year, eventShortName, categoryName, shortSessionName);
      if (analysis.isEmpty()) {
        throw new NotFoundException(String.format("Could not find analysis of session %s of category %s of %s in %d", shortSessionName, categoryName, eventShortName, year));
      }
      return SessionAnalysisOutput.from(session, analysis.get());

    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the analysis PDF for session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      throw new InternalServerErrorException(message, e);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{shortSessionName}/" + SessionOutput.TOPSPEED)
  public SessionTopSpeedsOutput getSessionTopSpeeds(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String categoryName, @PathParam("shortSessionName") String shortSessionName) {
    Session session = getSession(year, eventShortName, categoryName, shortSessionName);
    try {
      Optional<List<MaxSpeed>> topSpeeds = sessionService.getTopSpeeds(year, eventShortName, categoryName, shortSessionName);
      if (topSpeeds.isEmpty()) {
        throw new NotFoundException(String.format("Could not find analysis of session %s of category %s of %s in %d", shortSessionName, categoryName, eventShortName, year));
      }
      return SessionTopSpeedsOutput.from(session, topSpeeds.get());

    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the analysis PDF for session %s / %s of event %s of %d", shortSessionName, categoryName, eventShortName, year);
      throw new InternalServerErrorException(message, e);
    }
  }
}
