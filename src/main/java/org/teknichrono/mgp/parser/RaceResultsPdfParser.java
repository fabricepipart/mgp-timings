package org.teknichrono.mgp.parser;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.model.out.RaceClassificationDetails;
import org.teknichrono.mgp.model.result.RiderClassification;
import org.teknichrono.mgp.model.result.SessionClassification;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RaceResultsPdfParser {

  private static final Logger LOGGER = Logger.getLogger(RaceResultsPdfParser.class);

  public List<RaceClassificationDetails> parse(SessionClassification classifications) throws PdfParsingException {
    List<RaceClassificationDetails> toReturn = new ArrayList<>();
    List<RaceClassificationDetails> partialResults = getPartialResults(classifications.classification);
    fillFromPdf(toReturn, partialResults, classifications.file);
    return toReturn;
  }

  private List<RaceClassificationDetails> getPartialResults(List<RiderClassification> classifications) {
    List<RaceClassificationDetails> partialResults = new ArrayList<>();
    for (RiderClassification c : classifications) {
      RaceClassificationDetails details = RaceClassificationDetails.from(c);
      partialResults.add(details);
    }
    return partialResults;
  }

  private void fillFromPdf(List<RaceClassificationDetails> toReturn, List<RaceClassificationDetails> partialResults, String url) throws PdfParsingException {
    String[] lines = PdfParserUtils.readPdfLines(url);
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
  }
}
