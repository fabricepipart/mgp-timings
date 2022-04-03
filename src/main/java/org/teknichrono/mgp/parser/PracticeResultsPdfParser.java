package org.teknichrono.mgp.parser;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.model.out.PracticeClassificationDetails;
import org.teknichrono.mgp.model.out.SessionRider;
import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.result.SessionClassification;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class PracticeResultsPdfParser {

  private static final int BEST_LAP_NB_INDEX_AFTER_TIME = 1;
  private static final int GAP_TO_PREV_INDEX_AFTER_TIME = 4;

  private static final Logger LOGGER = Logger.getLogger(PracticeResultsPdfParser.class);

  public List<PracticeClassificationDetails> parse(SessionClassification classifications, List<SessionRider> ridersOfEvent) throws PdfParsingException {
    List<PracticeClassificationDetails> toReturn = new ArrayList<>();
    for (Classification c : classifications.classification) {
      PracticeClassificationDetails details = PracticeClassificationDetails.from(c, ridersOfEvent);
      toReturn.add(details);
    }
    String[] lines = PdfParserUtils.readPdfLines(classifications.file);

    for (String line : lines) {
      for (PracticeClassificationDetails details : toReturn) {
        String lowerCaseLine = line.toLowerCase();
        if (PdfParserUtils.startsWithNumber(line) &&
            lowerCaseLine.contains(details.riderNumber.toString()) &&
            lowerCaseLine.contains(details.riderName.toLowerCase())) {
          details.nation = PdfParserUtils.parseNation(line);
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
      }
    }

    return toReturn;
  }
}
