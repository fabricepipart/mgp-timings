package org.teknichrono.mgp.parser;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.model.out.RaceClassificationDetails;
import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.result.SessionClassification;
import org.teknichrono.mgp.model.rider.RiderDetails;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RaceResultsPdfParser {

  private static final Logger LOGGER = Logger.getLogger(RaceResultsPdfParser.class);

  public List<RaceClassificationDetails> parse(SessionClassification classifications, List<RiderDetails> ridersOfEvent, int year) throws PdfParsingException {
    List<RaceClassificationDetails> toReturn = new ArrayList<>();

    List<RaceClassificationDetails> partialResults = new ArrayList<>();
    for (Classification c : classifications.classification) {
      RaceClassificationDetails details = RaceClassificationDetails.from(c, ridersOfEvent, year);
      partialResults.add(details);
    }
    String[] lines = PdfParserUtils.readPdfLines(classifications.file);

    for (String line : lines) {
      for (RaceClassificationDetails details : partialResults) {
        String lowerCaseLine = line.toLowerCase();
        if (PdfParserUtils.startsWithNumber(line) &&
            lowerCaseLine.contains(details.riderNumber.toString()) &&
            lowerCaseLine.contains(details.riderName.toLowerCase())) {
          details.nation = PdfParserUtils.parseNation(line);
          details.averageSpeed = PdfParserUtils.parseSpeed(line);
          details.totalTime = PdfParserUtils.parseTime(line);
          toReturn.add(details);
        }
      }
    }

    return toReturn;
  }
}
