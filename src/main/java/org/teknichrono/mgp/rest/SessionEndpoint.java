package org.teknichrono.mgp.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.client.ResultsService;
import org.teknichrono.mgp.model.out.ClassificationDetails;
import org.teknichrono.mgp.model.out.MaxSpeed;
import org.teknichrono.mgp.model.out.PracticeClassificationDetails;
import org.teknichrono.mgp.model.out.RaceClassificationDetails;
import org.teknichrono.mgp.model.result.Category;
import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.result.Entry;
import org.teknichrono.mgp.model.result.Event;
import org.teknichrono.mgp.model.result.Session;
import org.teknichrono.mgp.model.result.SessionClassification;
import org.teknichrono.mgp.model.rider.RiderDetails;
import org.teknichrono.mgp.parser.MaxSpeedPdfParser;
import org.teknichrono.mgp.parser.PdfParsingException;
import org.teknichrono.mgp.parser.PracticeResultsPdfParser;
import org.teknichrono.mgp.parser.RaceResultsPdfParser;
import org.teknichrono.mgp.util.CsvConverter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.teknichrono.mgp.model.result.Session.FILENAME_MAX_SPEED;

@Path("/session")
public class SessionEndpoint {

  private static final Logger LOGGER = Logger.getLogger(SessionEndpoint.class);

  @Inject
  @RestClient
  ResultsService resultsService;

  @Inject
  RiderEndpoint riderEndpoint;

  @Inject
  EventEndpoint eventEndpoint;

  @Inject
  CategoryEndpoint categoryEndpoint;

  @Inject
  MaxSpeedPdfParser maxSpeedPdfParser;

  @Inject
  PracticeResultsPdfParser practiceResultsPdfParser;

  @Inject
  RaceResultsPdfParser raceResultsPdfParser;

  @Inject
  CsvConverter<Classification> csvConverter;

  @Inject
  CsvConverter<RaceClassificationDetails> raceCsvConverter;

  @Inject
  CsvConverter<PracticeClassificationDetails> practiceCsvConverter;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}")
  public List<Session> getSessions(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    Event event = eventEndpoint.eventsOfYear(year)
        .stream().filter(e -> eventShortName.equalsIgnoreCase(e.short_name)).findFirst().get();
    Category cat = categoryEndpoint.categoriesOfEvent(year, eventShortName)
        .stream().filter(c -> c.name.toLowerCase().contains(category.toLowerCase())).findFirst().get();
    return resultsService.getSessions(event.id, cat.id);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/riders")
  public List<Entry> getEntries(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    Event event = eventEndpoint.eventOfYear(year, eventShortName);
    Category cat = categoryEndpoint.categoryOfEvent(year, eventShortName, category);
    return resultsService.getEntries(event.id, cat.id).entry;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/ridersdetails")
  public List<RiderDetails> getRidersOfEvent(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category) {
    List<Entry> entries = getEntries(year, eventShortName, category);
    List<RiderDetails> details = new ArrayList<>();
    for (Entry e : entries) {
      details.add(riderEndpoint.getRider(e.rider.legacy_id));
    }
    return details;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}")
  public Session getSessionByName(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    List<Session> sessions = getSessions(year, eventShortName, category);
    return sessions.stream().filter(s -> s.getSessionName(s).equalsIgnoreCase(sessionShortName)).findFirst().get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}/topspeed")
  public List<MaxSpeed> sessionTopSpeeds(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    Session session = getSessionByName(year, eventShortName, category, sessionShortName);
    List<RiderDetails> ridersOfEvent = getRidersOfEvent(year, eventShortName, category);
    if (session.session_files.keySet().contains(FILENAME_MAX_SPEED) && session.session_files.get(FILENAME_MAX_SPEED).url != null) {
      String url = session.session_files.get(FILENAME_MAX_SPEED).url;
      try {
        return maxSpeedPdfParser.parse(url, ridersOfEvent, year);
      } catch (PdfParsingException e) {
        LOGGER.error("Error when parsing the PDF " + url, e);
        throw new InternalServerErrorException("Could not parse the PDF " + url, e);
      }
    }
    throw new NotFoundException();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}/results")
  public SessionClassification getClassifications(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    Session sessionMatch = getSessionByName(year, eventShortName, category, sessionShortName);
    return resultsService.getClassification(sessionMatch.id);
  }


  @GET
  @Path("/{year}/{eventShortName}/{category}/{session}/results/csv")
  @Produces("text/csv")
  @Transactional
  public Response listAllToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      String csvResults = csvConverter.convertToCsv(this.getClassifications(year, eventShortName, category, sessionShortName).classification, Classification.class);
      return Response.ok().entity(csvResults).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{year}/{eventShortName}/{category}/{session}/results/details")
  public List<? extends ClassificationDetails> getClassificationsPdfDetails(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    SessionClassification classifications = getClassifications(year, eventShortName, category, sessionShortName);
    List<RiderDetails> ridersOfEvent = getRidersOfEvent(year, eventShortName, category);
    try {
      if (sessionShortName.equalsIgnoreCase(Session.RACE_TYPE)) {
        return raceResultsPdfParser.parse(classifications, ridersOfEvent, year);
      } else {
        return practiceResultsPdfParser.parse(classifications, ridersOfEvent, year);
      }
    } catch (PdfParsingException e) {
      LOGGER.error("Error when parsing the PDF " + classifications.file, e);
      throw new InternalServerErrorException("Could not parse the PDF " + classifications.file, e);
    }
  }


  @GET
  @Path("/{year}/{eventShortName}/{category}/{session}/results/details/csv")
  @Produces("text/csv")
  @Transactional
  public Response getClassificationsPdfDetailsToCsv(@PathParam("year") int year, @PathParam("eventShortName") String eventShortName, @PathParam("category") String category, @PathParam("session") String sessionShortName) {
    try {
      String csvResults;
      if (sessionShortName.equalsIgnoreCase(Session.RACE_TYPE)) {
        List<RaceClassificationDetails> details = (List<RaceClassificationDetails>) getClassificationsPdfDetails(year, eventShortName, category, sessionShortName);
        csvResults = raceCsvConverter.convertToCsv(details, RaceClassificationDetails.class);
      } else {
        List<PracticeClassificationDetails> details = (List<PracticeClassificationDetails>) getClassificationsPdfDetails(year, eventShortName, category, sessionShortName);
        csvResults = practiceCsvConverter.convertToCsv(details, PracticeClassificationDetails.class);
      }
      return Response.ok().entity(csvResults).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }
}
