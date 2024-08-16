package org.teknichrono.mgp.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.MaxSpeed;
import org.teknichrono.mgp.api.model.SessionFilesOutput;
import org.teknichrono.mgp.api.model.SessionResultOutput;
import org.teknichrono.mgp.api.model.SessionRider;
import org.teknichrono.mgp.business.parser.AnalysisPdfParser;
import org.teknichrono.mgp.business.parser.MaxSpeedPdfParser;
import org.teknichrono.mgp.business.parser.PdfParsingException;
import org.teknichrono.mgp.business.parser.PracticeResultsPdfParser;
import org.teknichrono.mgp.business.parser.RaceResultsPdfParser;
import org.teknichrono.mgp.business.parser.ResultsPdfParser;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.client.model.result.SessionResults;
import org.teknichrono.mgp.client.rest.ResultsClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.teknichrono.mgp.client.model.result.SessionFileType.ANALYSIS;
import static org.teknichrono.mgp.client.model.result.SessionFileType.CLASSIFICATION;
import static org.teknichrono.mgp.client.model.result.SessionFileType.MAXIMUM_SPEED;

@ApplicationScoped
public class SessionService {

  @Inject
  @RestClient
  ResultsClient resultsClient;

  @Inject
  EventService eventService;

  @Inject
  CategoryService categoryService;

  @Inject
  RiderService riderService;

  @Inject
  RaceResultsPdfParser raceResultsPdfParser;

  @Inject
  PracticeResultsPdfParser practiceResultsPdfParser;

  @Inject
  MaxSpeedPdfParser maxSpeedPdfParser;

  @Inject
  AnalysisPdfParser analysisPdfParser;

  public List<Session> getSessions(int year, String eventShortName, String category) {
    Optional<Event> event = eventService.getEvent(year, eventShortName);
    if (event.isEmpty()) {
      return Collections.emptyList();
    }
    Optional<Category> catOptional = categoryService.categoriesOfEvent(year, eventShortName)
        .stream().filter(c -> c.name.toLowerCase().contains(category.toLowerCase())).findFirst();
    if (catOptional.isEmpty()) {
      return Collections.emptyList();
    }
    Category cat = catOptional.get();
    List<Session> sessions = resultsClient.getSessions(event.get().id, cat.id);
    sessions.forEach(s -> s.test = event.get().test);
    return sessions;
  }

  public Optional<Session> getSessionByName(int year, String eventShortName, String category, String sessionShortName) {
    List<Session> sessions = getSessions(year, eventShortName, category);
    if (sessions == null || sessions.isEmpty()) {
      return Optional.empty();
    }
    return sessions.stream().filter(s -> s.getSessionName().equalsIgnoreCase(sessionShortName)).findFirst();
  }

  public Optional<SessionResults> getResults(int year, String eventShortName, String category, String sessionShortName) {
    Optional<Session> sessionMatch = getSessionByName(year, eventShortName, category, sessionShortName);
    if (sessionMatch.isEmpty()) {
      return Optional.empty();
    }
    Session session = sessionMatch.get();
    SessionResults classification = resultsClient.getClassification(session.id, session.test);
    // Sometimes the returned classification is actually empty
    if (classification == null || classification.classification == null || classification.classification.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(classification);
  }

  public SessionResultOutput getResultDetails(int year, String eventShortName, String category, String sessionShortName) throws PdfParsingException {
    Optional<Session> sessionMatch = getSessionByName(year, eventShortName, category, sessionShortName);
    // Session must exist
    if (sessionMatch.isEmpty()) {
      throw new NotFoundException(String.format("Could not find the session %s / %s of event %s of %d", sessionShortName, category, eventShortName, year));
    }
    Session session = sessionMatch.get();
    SessionResultOutput output = SessionResultOutput.from(session);

    // We must have at least one classification source
    Optional<SessionResults> optionalResults = getResults(year, eventShortName, category, sessionShortName);
    String pdfUrl = getPdfUrl(sessionMatch, optionalResults);
    if (optionalResults.isEmpty() && (pdfUrl == null || pdfUrl.isEmpty())) {
      throw new NotFoundException(String.format("Could not find the results details for session %s / %s of event %s of %d", sessionShortName, category, eventShortName, year));
    }

    // Fill data with API results
    if (optionalResults.isPresent()) {
      SessionResults results = optionalResults.get();
      output.fillRecords(results);
      output.fillWith(results.classification);
    }

    // Fill data with PDF results
    if (pdfUrl != null && !pdfUrl.isEmpty()) {
      List<SessionRider> ridersOfEvent = riderService.getRidersOfEvent(year, eventShortName, category).orElse(Collections.emptyList());
      getPdfParser(sessionShortName).parseAndComplete(output, ridersOfEvent, pdfUrl);
    }
    return output;
  }

  static String getPdfUrl(Optional<Session> optionalSession, Optional<SessionResults> optionalResults) {
    if (optionalResults.isPresent()) {
      SessionResults results = optionalResults.get();
      if (results.files != null) {
        return results.files.classification;
      }
      if (results.file != null) {
        return results.file;
      }
    }
    if (optionalSession.isPresent()) {
      Session session = optionalSession.get();
      if (session.session_files != null && session.session_files.get(CLASSIFICATION) != null) {
        return SessionFilesOutput.getUrlFromMap(session.session_files, CLASSIFICATION);
      }
    }
    return null;
  }

  private ResultsPdfParser getPdfParser(String sessionShortName) {
    if (sessionShortName.equalsIgnoreCase(Session.RACE_TYPE)) {
      return raceResultsPdfParser;
    }
    return practiceResultsPdfParser;
  }

  public Optional<List<LapAnalysis>> getAnalysis(int year, String eventShortName, String category, String sessionShortName) throws PdfParsingException {
    Optional<Session> sessionOptional = getSessionByName(year, eventShortName, category, sessionShortName);
    Optional<SessionResults> classificationsOptional = getResults(year, eventShortName, category, sessionShortName);
    if (sessionOptional.isEmpty() || classificationsOptional.isEmpty()) {
      return Optional.empty();
    }
    Session session = sessionOptional.get();
    SessionResults classifications = classificationsOptional.get();
    if (session.session_files.keySet().contains(ANALYSIS)) {
      String url = SessionFilesOutput.getUrlFromMap(session.session_files, ANALYSIS);
      return Optional.of(analysisPdfParser.parse(url, classifications.classification));

    }
    return Optional.empty();
  }

  public Optional<List<MaxSpeed>> getTopSpeeds(int year, String eventShortName, String category, String sessionShortName) throws PdfParsingException {
    Optional<Session> sessionOptional = getSessionByName(year, eventShortName, category, sessionShortName);
    Optional<SessionResults> classificationsOptional = getResults(year, eventShortName, category, sessionShortName);
    if (sessionOptional.isEmpty() || classificationsOptional.isEmpty()) {
      return Optional.empty();
    }
    Session session = sessionOptional.get();
    SessionResults classifications = classificationsOptional.get();
    if (session.session_files.keySet().contains(MAXIMUM_SPEED)) {
      String url = SessionFilesOutput.getUrlFromMap(session.session_files, MAXIMUM_SPEED);
      return Optional.of(maxSpeedPdfParser.parse(url, classifications.classification));
    }
    return Optional.empty();
  }
}
