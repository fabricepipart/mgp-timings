package org.teknichrono.mgp.parser;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.model.out.PracticeClassificationDetails;
import org.teknichrono.mgp.model.result.RiderClassification;
import org.teknichrono.mgp.model.result.SessionClassification;
import org.teknichrono.mgp.model.result.TestClassification;
import org.teknichrono.mgp.model.rider.RiderDetails;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class PracticeResultsPdfParser {

  private static final int BEST_LAP_NB_INDEX_AFTER_TIME = 1;
  private static final int GAP_TO_PREV_INDEX_AFTER_TIME = 4;

  private static final Logger LOGGER = Logger.getLogger(PracticeResultsPdfParser.class);

  public List<PracticeClassificationDetails> parse(TestClassification classifications, List<RiderDetails> ridersOfEvent, int year) throws PdfParsingException {
    List<PracticeClassificationDetails> toReturn = getPartialResults(classifications.classification, ridersOfEvent, year);
    fillFromPdf(toReturn, classifications.files.classification);
    return toReturn;

  }

  public List<PracticeClassificationDetails> parse(SessionClassification classifications, List<RiderDetails> ridersOfEvent, int year) throws PdfParsingException {
    List<PracticeClassificationDetails> toReturn = getPartialResults(classifications.classification, ridersOfEvent, year);
    fillFromPdf(toReturn, classifications.file);

    return toReturn;
  }

  private void fillFromPdf(List<PracticeClassificationDetails> toReturn, String url) throws PdfParsingException {
    String[] lines = PdfParserUtils.readPdfLines(url);

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
  }

  private List<PracticeClassificationDetails> getPartialResults(List<RiderClassification> classifications, List<RiderDetails> ridersOfEvent, int year) {
    List<PracticeClassificationDetails> toReturn = new ArrayList<>();
    for (RiderClassification c : classifications) {
      PracticeClassificationDetails details = PracticeClassificationDetails.from(c, ridersOfEvent, year);
      toReturn.add(details);
    }
    return toReturn;
  }
}
