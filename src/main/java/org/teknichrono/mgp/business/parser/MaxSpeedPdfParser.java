package org.teknichrono.mgp.business.parser;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.api.model.MaxSpeed;
import org.teknichrono.mgp.client.model.result.RiderClassification;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MaxSpeedPdfParser {
  private static final Logger LOGGER = Logger.getLogger(MaxSpeedPdfParser.class);


  public List<MaxSpeed> parse(String url, List<RiderClassification> ridersOfEvent, int year) throws PdfParsingException {
    List<MaxSpeed> toReturn = new ArrayList<>();
    if (url != null) {
      String[] lines = PdfParserUtils.readPdfLines(url);
      boolean start = false;
      boolean endReached = false;
      for (String line : lines) {
        endReached = endReached || (start && !Character.isDigit(line.charAt(0)));
        if (start && !endReached) {
          toReturn.add(parseLine(line, ridersOfEvent, year));
        }
        if (line.startsWith("Rider")) {
          start = true;
        }
      }
      assertDocumentCorrectlyParsed(url, start, endReached);
    }
    return toReturn;
  }

  private void assertDocumentCorrectlyParsed(String url, boolean start, boolean endReached) throws PdfParsingException {
    if (!start || !endReached) {
      throw new PdfParsingException("Could not find the expected lines format in the PDF at " + url);
    }
  }

  MaxSpeed parseLine(String line, List<RiderClassification> ridersOfEvent, int year) throws PdfParsingException {
    MaxSpeed maxSpeed = new MaxSpeed();
    parseRider(line, maxSpeed, ridersOfEvent, year);
    maxSpeed.speed = PdfParserUtils.parseSpeed(line);
    maxSpeed.nation = PdfParserUtils.parseNation(line);
    if (maxSpeed.testIfIncomplete()) {
      throw new PdfParsingException("After parsing " + line + " all we could parse was: " + maxSpeed);
    }
    return maxSpeed;
  }

  private void parseRider(String line, MaxSpeed maxSpeed, List<RiderClassification> ridersOfEvent, int year) {
    for (RiderClassification classification : ridersOfEvent) {
      if (line.startsWith(classification.rider.number.toString())) {
        maxSpeed.number = classification.rider.number;
        maxSpeed.rider = classification.rider.full_name;
        maxSpeed.team = classification.team.name;
        maxSpeed.motorcycle = classification.constructor.name;
      }
    }
  }

}
