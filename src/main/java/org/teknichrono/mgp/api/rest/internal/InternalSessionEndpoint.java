package org.teknichrono.mgp.api.rest.internal;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.MaxSpeed;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.api.model.SessionRider;
import org.teknichrono.mgp.business.parser.PdfParsingException;
import org.teknichrono.mgp.business.service.RiderService;
import org.teknichrono.mgp.business.service.SessionService;
import org.teknichrono.mgp.client.model.result.Classification;
import org.teknichrono.mgp.client.model.result.Entry;
import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.csv.model.RiderClassificationCSV;
import org.teknichrono.mgp.csv.model.SessionClassificationCSV;
import org.teknichrono.mgp.csv.util.CsvConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.teknichrono.mgp.csv.util.CsvConverter.ATTACHMENT_FILENAME;
import static org.teknichrono.mgp.csv.util.CsvConverter.CONTENT_DISPOSITION_HEADER;

@Path("/internal/session")
public class InternalSessionEndpoint {

  private static final Logger LOGGER = Logger.getLogger(InternalSessionEndpoint.class);

  @Inject
  RiderService riderService;

  @Inject
  SessionService sessionService;

  @Inject
  CsvConverterFactory csvConverterFactory;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}")
  public List<Session> getSessions(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    return sessionService.getSessions(year, eventShortName, category);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Deprecated(forRemoval = true)
  @Path("/test/{year}/{eventShortName}/{category}")
  public List<Session> getTestSessions(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    return getSessions(year, eventShortName, category);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}")
  public Session getSessionByName(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    Optional<Session> sessionByName = sessionService.getSessionByName(year, eventShortName, category, sessionShortName);
    if (sessionByName.isEmpty()) {
      throw new NotFoundException(String.format("Could not find the session %s for category %s for event %s of %d", sessionShortName, category, eventShortName, year));
    }
    return sessionByName.get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Deprecated(forRemoval = true)
  @Path("/test/{year}/{eventShortName}/{category}/{session}")
  public Session getTestSessionByName(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    return getSessionByName(year, eventShortName, category, sessionShortName);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/riders")
  public List<Entry> getEntries(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    Optional<List<Entry>> entries = riderService.getEntries(year, eventShortName, category);
    if (entries.isEmpty()) {
      throw new NotFoundException(String.format("Could not find the entries %s for event %s of %d", category, eventShortName, year));
    }
    return entries.get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/ridersdetails")
  public List<SessionRider> getRidersOfSession(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    Optional<List<SessionRider>> riders = riderService.getRidersOfEvent(year, eventShortName, category);
    if (riders.isEmpty()) {
      throw new NotFoundException(String.format("Could not find the riders %s for event %s of %d", category, eventShortName, year));
    }
    return riders.get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}/topspeed")
  public List<MaxSpeed> getTopSpeeds(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      Optional<List<MaxSpeed>> analysis = sessionService.getTopSpeeds(year, eventShortName, category, sessionShortName);
      if (analysis.isEmpty()) {
        throw new NotFoundException();
      }
      return analysis.get();
    } catch (PdfParsingException e) {
      throw new InternalServerErrorException("Could not parse the PDF", e);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}/results")
  public Classification getClassifications(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    Optional<Classification> results = sessionService.getResults(year, eventShortName, category, sessionShortName);
    if (results.isEmpty()) {
      throw new NotFoundException(String.format("Could not find the results for session %s / %s of event %s of %d", sessionShortName, category, eventShortName, year));
    }
    return results.get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Deprecated(forRemoval = true)
  @Path("/test/{year}/{eventShortName}/{category}/{session}/results")
  public Classification getTestClassifications(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    return getClassifications(year, eventShortName, category, sessionShortName);
  }


  @GET
  @Path("/{year}/{eventShortName}/{category}/{session}/results/csv")
  @Produces("text/csv")
  @Transactional
  public Response listAllToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      List<RiderClassification> classification = this.getClassifications(year, eventShortName, category, sessionShortName).classification;
      String csvResults = csvConverterFactory.getRiderCsvConverter().convertToCsv(classification, RiderClassificationCSV.class);
      String filename = String.format("sessions-%d-%s-%s-%s.csv", year, eventShortName.toLowerCase(), category.toLowerCase(), sessionShortName.toLowerCase());
      return Response.ok().entity(csvResults).header(CONTENT_DISPOSITION_HEADER, ATTACHMENT_FILENAME + filename).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}/results/details")
  public List<SessionClassificationOutput> getClassificationsPdfDetails(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      List<SessionClassificationOutput> results = sessionService.getResultDetails(year, eventShortName, category, sessionShortName);
      return results;
    } catch (PdfParsingException e) {
      String message = String.format("Error when parsing the PDF for session %s / %s of event %s of %d", sessionShortName, category, eventShortName, year);
      LOGGER.error(message, e);
      throw new InternalServerErrorException(message, e);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/test/{year}/{eventShortName}/{category}/{session}/results/details")
  public List<SessionClassificationOutput> getTestClassificationsPdfDetails(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    return getClassificationsPdfDetails(year, eventShortName, category, sessionShortName);
  }

  @GET
  @Path("/{year}/{eventShortName}/{category}/{session}/results/details/csv")
  @Produces("text/csv")
  @Transactional
  public Response getClassificationsPdfDetailsToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      String csvResults;
      List<SessionClassificationOutput> details = getClassificationsPdfDetails(year, eventShortName, category, sessionShortName);
      csvResults = csvConverterFactory.getClassificationCsvConverter().convertToCsv(details, SessionClassificationCSV.class);
      String filename = String.format("classification-%d-%s-%s-%s.csv", year, eventShortName.toLowerCase(), category.toLowerCase(), sessionShortName.toLowerCase());
      return Response.ok().entity(csvResults).header(CONTENT_DISPOSITION_HEADER, ATTACHMENT_FILENAME + filename).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }

  @GET
  @Path("/test/{year}/{eventShortName}/{category}/{session}/results/details/csv")
  @Produces("text/csv")
  @Transactional
  public Response getTestClassificationsPdfDetailsToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      List<SessionClassificationOutput> details = getTestClassificationsPdfDetails(year, eventShortName, category, sessionShortName);
      String csvResults = csvConverterFactory.getClassificationCsvConverter().convertToCsv(details, SessionClassificationCSV.class);
      String filename = String.format("test-classification-%d-%s-%s-%s.csv", year, eventShortName.toLowerCase(), category.toLowerCase(), sessionShortName.toLowerCase());
      return Response.ok().entity(csvResults).header(CONTENT_DISPOSITION_HEADER, ATTACHMENT_FILENAME + filename).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}/analysis")
  public List<LapAnalysis> getAnalysis(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      Optional<List<LapAnalysis>> analysis = sessionService.getAnalysis(year, eventShortName, category, sessionShortName);
      if (analysis.isEmpty()) {
        throw new NotFoundException();
      }
      return analysis.get();
    } catch (PdfParsingException e) {
      throw new InternalServerErrorException("Could not parse the PDF", e);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Deprecated(forRemoval = true)
  @Path("/test/{year}/{eventShortName}/{category}/{session}/analysis")
  public List<LapAnalysis> getTestAnalysis(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    return getAnalysis(year, eventShortName, category, sessionShortName);
  }


  @GET
  @Path("/{year}/{eventShortName}/{category}/{session}/analysis/csv")
  @Produces("text/csv")
  @Transactional
  public Response getAnalysisToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      List<LapAnalysis> lapAnalysis = getAnalysis(year, eventShortName, category, sessionShortName);
      String csvResults = csvConverterFactory.getLapAnalysisCsvConverter().convertToCsv(lapAnalysis, LapAnalysis.class);
      String filename = String.format("analysis-%d-%s-%s-%s.csv", year, eventShortName.toLowerCase(), category.toLowerCase(), sessionShortName.toLowerCase());
      return Response.ok().entity(csvResults).header(CONTENT_DISPOSITION_HEADER, ATTACHMENT_FILENAME + filename).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }


  @GET
  @Path("/test/{year}/{eventShortName}/{category}/{session}/analysis/csv")
  @Produces("text/csv")
  @Transactional
  public Response getTestAnalysisToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      List<LapAnalysis> lapAnalysis = getTestAnalysis(year, eventShortName, category, sessionShortName);
      String csvResults = csvConverterFactory.getLapAnalysisCsvConverter().convertToCsv(lapAnalysis, LapAnalysis.class);
      String filename = String.format("test-analysis-%d-%s-%s-%s.csv", year, eventShortName.toLowerCase(), category.toLowerCase(), sessionShortName.toLowerCase());
      return Response.ok().entity(csvResults).header(CONTENT_DISPOSITION_HEADER, ATTACHMENT_FILENAME + filename).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }
}
