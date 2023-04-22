package org.teknichrono.mgp.business;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.api.client.ResultsClient;
import org.teknichrono.mgp.api.model.result.Category;
import org.teknichrono.mgp.api.model.result.Classification;
import org.teknichrono.mgp.api.model.result.Event;
import org.teknichrono.mgp.api.model.result.Session;
import org.teknichrono.mgp.model.out.LapAnalysis;
import org.teknichrono.mgp.model.out.MaxSpeed;
import org.teknichrono.mgp.model.output.SessionClassificationOutput;
import org.teknichrono.mgp.parser.AnalysisPdfParser;
import org.teknichrono.mgp.parser.MaxSpeedPdfParser;
import org.teknichrono.mgp.parser.PdfParsingException;
import org.teknichrono.mgp.parser.PracticeResultsPdfParser;
import org.teknichrono.mgp.parser.RaceResultsPdfParser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

import static org.teknichrono.mgp.api.model.result.SessionFileType.analysis;
import static org.teknichrono.mgp.api.model.result.SessionFileType.maximum_speed;

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
    Optional<Event> event = eventService.getEventOrTestOfYear(year, eventShortName);
    if (event.isEmpty()) {
      return null;
    }
    Category cat = categoryService.categoriesOfEvent(year, eventShortName)
        .stream().filter(c -> c.name.toLowerCase().contains(category.toLowerCase())).findFirst().get();
    List<Session> sessions = resultsClient.getSessions(event.get().id, cat.id);
    sessions.forEach(s -> s.test = event.get().test);
    return sessions;
  }

  public Optional<Session> getSessionByName(int year, String eventShortName, String category, String sessionShortName) {
    List<Session> sessions = getSessions(year, eventShortName, category);
    return sessions.stream().filter(s -> s.getSessionName().equalsIgnoreCase(sessionShortName)).findFirst();
  }

  public Optional<Classification> getResults(int year, String eventShortName, String category, String sessionShortName) {
    Optional<Session> sessionMatch = getSessionByName(year, eventShortName, category, sessionShortName);
    if (sessionMatch.isEmpty()) {
      return Optional.empty();
    }
    Session session = sessionMatch.get();
    if (session.test) {
      return Optional.of(resultsClient.getTestClassification(session.id));
    }
    return Optional.of(resultsClient.getClassification(session.id));
  }

  public List<SessionClassificationOutput> getResultDetails(int year, String eventShortName, String category, String sessionShortName) throws PdfParsingException {
    Optional<Classification> results = getResults(year, eventShortName, category, sessionShortName);
    if (results.isEmpty()) {
      throw new NotFoundException(String.format("Could not find the results details for session %s / %s of event %s of %d", sessionShortName, category, eventShortName, year));
    }
    Classification classifications = results.get();
    if (sessionShortName.equalsIgnoreCase(Session.RACE_TYPE)) {
      return raceResultsPdfParser.parse(classifications);
    } else {
      return practiceResultsPdfParser.parse(classifications);
    }
  }

  public Optional<List<LapAnalysis>> getAnalysis(int year, String eventShortName, String category, String sessionShortName) throws PdfParsingException {
    Optional<Session> sessionOptional = getSessionByName(year, eventShortName, category, sessionShortName);
    if (sessionOptional.isEmpty()) {
      return Optional.empty();
    }
    Session session = sessionOptional.get();
    Optional<Classification> classificationsOptional = getResults(year, eventShortName, category, sessionShortName);
    if (classificationsOptional.isEmpty()) {
      return Optional.empty();
    }
    Classification classifications = classificationsOptional.get();
    if (session.session_files.keySet().contains(analysis) && session.session_files.get(analysis).url != null) {
      String url = session.session_files.get(analysis).url;
      return Optional.of(analysisPdfParser.parse(url, classifications.classification));

    }
    return Optional.empty();
  }

  public Optional<List<MaxSpeed>> getTopSpeeds(int year, String eventShortName, String category, String sessionShortName) throws PdfParsingException {
    Optional<Session> sessionOptional = getSessionByName(year, eventShortName, category, sessionShortName);
    if (sessionOptional.isEmpty()) {
      return Optional.empty();
    }
    Session session = sessionOptional.get();
    Optional<Classification> classificationsOptional = getResults(year, eventShortName, category, sessionShortName);
    if (classificationsOptional.isEmpty()) {
      return Optional.empty();
    }
    Classification classifications = classificationsOptional.get();
    if (session.session_files.keySet().contains(maximum_speed) && session.session_files.get(maximum_speed).url != null) {
      String url = session.session_files.get(maximum_speed).url;
      return Optional.of(maxSpeedPdfParser.parse(url, classifications.classification, year));

    }
    return Optional.empty();
  }
}
