package org.teknichrono.mgp.business.parser;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.client.model.result.Classification;
import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.api.model.CountryOutput;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RaceResultsPdfParser {

  private static final Logger LOGGER = Logger.getLogger(RaceResultsPdfParser.class);

  public List<SessionClassificationOutput> parse(Classification classifications) throws PdfParsingException {
    List<SessionClassificationOutput> partialResults = getPartialResults(classifications.classification);
    return fillFromPdf(partialResults, classifications.file);
  }

  private List<SessionClassificationOutput> getPartialResults(List<RiderClassification> classifications) {
    List<SessionClassificationOutput> partialResults = new ArrayList<>();
    for (RiderClassification c : classifications) {
      SessionClassificationOutput details = SessionClassificationOutput.from(c);
      partialResults.add(details);
    }
    return partialResults;
  }

  private List<SessionClassificationOutput> fillFromPdf(List<SessionClassificationOutput> partialResults, String url) throws PdfParsingException {
    List<SessionClassificationOutput> toReturn = new ArrayList<>();
    String[] lines = PdfParserUtils.readPdfLines(url);
    for (String line : lines) {
      for (SessionClassificationOutput details : partialResults) {
        String lowerCaseLine = line.toLowerCase();
        if (PdfParserUtils.startsWithNumber(line) &&
            details.rider != null && details.rider.number != null &&
            lowerCaseLine.contains(details.rider.number.toString()) &&
            lowerCaseLine.contains(details.rider.full_name.toLowerCase())) {
          details.rider.country = CountryOutput.from(PdfParserUtils.parseNation(line));
          details.averageSpeed = PdfParserUtils.parseSpeed(line);
          details.totalTime = PdfParserUtils.parseTime(line);
          toReturn.add(details);
        }
      }
    }
    return toReturn;
  }
}
