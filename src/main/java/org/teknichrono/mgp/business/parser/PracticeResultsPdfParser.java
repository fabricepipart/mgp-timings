package org.teknichrono.mgp.business.parser;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.api.model.CountryOutput;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.client.model.result.SessionResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class PracticeResultsPdfParser {

  private static final int BEST_LAP_NB_INDEX_AFTER_TIME = 1;
  private static final int GAP_TO_PREV_INDEX_AFTER_TIME = 4;

  private static final Logger LOGGER = Logger.getLogger(PracticeResultsPdfParser.class);

  public List<SessionClassificationOutput> parse(SessionResults classifications) throws PdfParsingException {
    List<SessionClassificationOutput> toReturn = getPartialResults(classifications.classification);
    if (classifications.files != null) {
      fillFromPdf(toReturn, classifications.files.classification);
    }
    if (classifications.file != null) {
      fillFromPdf(toReturn, classifications.file);
    }
    return toReturn;
  }

  private void fillFromPdf(List<SessionClassificationOutput> toReturn, String url) throws PdfParsingException {
    String[] lines = PdfParserUtils.readPdfLines(url);

    for (String line : lines) {
      for (SessionClassificationOutput details : toReturn) {
        String lowerCaseLine = line.toLowerCase();
        if (match(details, lowerCaseLine)) {
          fillOutputWithLineContent(line, details);
        }
      }
    }
  }

  private boolean match(SessionClassificationOutput details, String lowerCaseLine) {
    return PdfParserUtils.startsWithNumber(lowerCaseLine) &&
        details.rider != null && details.rider.number != null &&
        lowerCaseLine.contains(details.rider.number.toString()) &&
        lowerCaseLine.contains(details.rider.full_name.toLowerCase());
  }

  private void fillOutputWithLineContent(String line, SessionClassificationOutput details) {
    details.rider.country = CountryOutput.from(PdfParserUtils.parseNation(line));
    details.topSpeed = PdfParserUtils.parseSpeed(line);
    details.bestLapTime = PdfParserUtils.parseTime(line);

    String[] words = line.split(" ");
    if (details.bestLapTime != null) {
      int bestLapTimeIndex = Arrays.asList(words).indexOf(details.bestLapTime);
      if (words.length > (bestLapTimeIndex + BEST_LAP_NB_INDEX_AFTER_TIME)) {
        details.bestLapNumber = PdfParserUtils.parseIntegerIfYouCan(words[bestLapTimeIndex + BEST_LAP_NB_INDEX_AFTER_TIME]);
      }
      if (words.length > (bestLapTimeIndex + GAP_TO_PREV_INDEX_AFTER_TIME)) {
        details.gapToPrevious = PdfParserUtils.parseFloatIfYouCan(words[bestLapTimeIndex + GAP_TO_PREV_INDEX_AFTER_TIME]);
      }
    }
    LOGGER.debug("Words : " + Arrays.toString(words));
  }

  private List<SessionClassificationOutput> getPartialResults(List<RiderClassification> classifications) {
    List<SessionClassificationOutput> toReturn = new ArrayList<>();
    for (RiderClassification c : classifications) {
      SessionClassificationOutput details = SessionClassificationOutput.from(c);
      toReturn.add(details);
    }
    return toReturn;
  }
}
