package org.teknichrono.mgp.business.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.MaxSpeed;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.api.model.SessionFilesOutput;
import org.teknichrono.mgp.business.parser.AnalysisPdfParser;
import org.teknichrono.mgp.business.parser.MaxSpeedPdfParser;
import org.teknichrono.mgp.business.parser.PdfParsingException;
import org.teknichrono.mgp.business.parser.PracticeResultsPdfParser;
import org.teknichrono.mgp.business.parser.RaceResultsPdfParser;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.client.model.result.SessionResults;
import org.teknichrono.mgp.client.rest.ResultsClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.teknichrono.mgp.client.model.result.SessionFileType.ANALYSIS;
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
    if (session.test) {
      return Optional.of(resultsClient.getClassification(session.id, true));
    }
    return Optional.of(resultsClient.getClassification(session.id, false));
  }

  public List<SessionClassificationOutput> getResultDetails(int year, String eventShortName, String category, String sessionShortName) throws PdfParsingException {
    Optional<SessionResults> results = getResults(year, eventShortName, category, sessionShortName);
    if (results.isEmpty()) {
      throw new NotFoundException(String.format("Could not find the results details for session %s / %s of event %s of %d", sessionShortName, category, eventShortName, year));
    }
    SessionResults classifications = results.get();
    if (sessionShortName.equalsIgnoreCase(Session.RACE_TYPE)) {
      return raceResultsPdfParser.parse(classifications);
    } else {
      return practiceResultsPdfParser.parse(classifications);
    }
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
