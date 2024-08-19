package org.teknichrono.mgp.business.parser;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.api.model.RiderOutput;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.api.model.SessionResultOutput;
import org.teknichrono.mgp.api.model.SessionRider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class ResultsPdfParser {

  private static final Logger LOGGER = Logger.getLogger(ResultsPdfParser.class);

  public void parseAndComplete(SessionResultOutput sessionResults, List<SessionRider> ridersOfEvent, String url) throws PdfParsingException {
    String[] lines = PdfParserUtils.readPdfLines(url);
    for (String line : lines) {
      fillClassificationDataIfNecessary(line, sessionResults.classifications, ridersOfEvent);
      fillSessionDataIfNecessary(line, sessionResults);
    }
  }

  protected abstract void fillSessionDataIfNecessary(String line, SessionResultOutput sessionResults);

  private void fillClassificationDataIfNecessary(String line, List<SessionClassificationOutput> classifications, List<SessionRider> ridersOfEvent) {
    Optional<SessionClassificationOutput> matchingResult = classifications.stream().filter(c -> lineAndResultMatch(line, c)).findFirst();
    if (matchingResult.isPresent()) {
      completeResultInfo(line, matchingResult.get());
    } else {
      Optional<SessionRider> matchingRider = ridersOfEvent.stream().filter(r -> lineAndRiderMatch(line, r)).findFirst();
      if (matchingRider.isPresent()) {
        SessionClassificationOutput result = createResultInfo(line, matchingRider.get());
        classifications.add(result);
      }
    }

    // Already present
    for (SessionClassificationOutput details : classifications) {
      if (lineAndResultMatch(line, details)) {
        completeResultInfo(line, details);
      }
    }
  }

  protected static void parseIntegersOfLine(SessionClassificationOutput details, String firstPartOfLine) {
    List<String> integers = PdfParserUtils.parseIntegers(firstPartOfLine);
    switch (integers.size()) {
      case 1:
        details.rider.number = Integer.parseInt(integers.get(0));
        break;
      case 2:
        details.position = Integer.parseInt(integers.get(0));
        details.rider.number = Integer.parseInt(integers.get(1));
        break;
      case 3:
        details.position = Integer.parseInt(integers.get(0));
        details.points = Integer.parseInt(integers.get(1));
        details.rider.number = Integer.parseInt(integers.get(2));
        break;
      default:
        LOGGER.warn("Unexpected number of Integers found in line " + firstPartOfLine);
        break;
    }
  }


  private static boolean lineContainsNumberAndWords(String line, String... words) {
    String lowerCaseLine = line.toLowerCase();
    return PdfParserUtils.startsWithNumber(line) && Stream.of(words).allMatch(lowerCaseLine::contains);
  }

  static boolean lineAndResultMatch(String line, SessionClassificationOutput c) {
    if (c.rider == null || c.rider.number == null) {
      return false;
    }
    return lineContainsNumberAndWords(line, c.rider.number.toString(), c.rider.full_name.toLowerCase());
  }

  private static boolean lineAndRiderMatch(String line, SessionRider r) {
    if (r == null || r.number == null) {
      return false;
    }
    return lineContainsNumberAndWords(line, r.name.toLowerCase(), r.surname.toLowerCase());
  }

  protected abstract void completeResultInfo(String line, SessionClassificationOutput sessionClassificationOutput);

  private SessionClassificationOutput createResultInfo(String line, SessionRider sessionRider) {
    SessionClassificationOutput result = new SessionClassificationOutput();
    result.rider = RiderOutput.from(sessionRider);
    result.team = sessionRider.team;
    result.constructor = sessionRider.constructor;
    completeResultInfo(line, result);
    return result;
  }
}
