package org.teknichrono.mgp.business.parser;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.api.model.CountryOutput;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.api.model.SessionResultOutput;

import java.util.Arrays;
import java.util.Optional;

@ApplicationScoped
public class PracticeResultsPdfParser extends ResultsPdfParser {

  private static final int BEST_LAP_NB_INDEX_AFTER_TIME = 1;
  private static final int LAP_NB_INDEX_AFTER_TIME = 2;
  private static final int GAP_TO_FIRST_INDEX_AFTER_TIME = 3;
  private static final int GAP_TO_PREV_INDEX_AFTER_TIME = 4;

  private static final Logger LOGGER = Logger.getLogger(PracticeResultsPdfParser.class);

  @Override
  protected void fillSessionDataIfNecessary(String line, SessionResultOutput sessionResults) {
    LOGGER.debug("Nothing related to session is available in the Practice PDFs");
  }

  @Override
  protected void completeResultInfo(String line, SessionClassificationOutput details) {
    String nation = PdfParserUtils.parseNation(line);
    details.rider.country = CountryOutput.from(nation);

    details.topSpeed = Optional.ofNullable(details.topSpeed).orElse(PdfParserUtils.parseSpeed(line));
    details.bestLapTime = PdfParserUtils.parseTime(line);

    parseIntegersOfLine(details, line.split(nation)[0]);

    String[] words = line.split(" ");
    if (details.bestLapTime != null) {
      int bestLapTimeIndex = Arrays.asList(words).indexOf(details.bestLapTime);
      if (words.length > (bestLapTimeIndex + BEST_LAP_NB_INDEX_AFTER_TIME)) {
        details.bestLapNumber = PdfParserUtils.parseIntegerIfYouCan(words[bestLapTimeIndex + BEST_LAP_NB_INDEX_AFTER_TIME]);
      }
      if (words.length > (bestLapTimeIndex + LAP_NB_INDEX_AFTER_TIME)) {
        details.totalLaps = PdfParserUtils.parseIntegerIfYouCan(words[bestLapTimeIndex + LAP_NB_INDEX_AFTER_TIME]);
      }
      if (words.length > (bestLapTimeIndex + GAP_TO_FIRST_INDEX_AFTER_TIME) && details.position > 1) {
        details.gapToFirst = PdfParserUtils.parseFloatIfYouCan(words[bestLapTimeIndex + GAP_TO_FIRST_INDEX_AFTER_TIME]);
      }
      if (words.length > (bestLapTimeIndex + GAP_TO_PREV_INDEX_AFTER_TIME)) {
        details.gapToPrevious = PdfParserUtils.parseFloatIfYouCan(words[bestLapTimeIndex + GAP_TO_PREV_INDEX_AFTER_TIME]);
      }
    }
  }

}
