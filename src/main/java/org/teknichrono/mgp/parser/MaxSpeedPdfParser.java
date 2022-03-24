package org.teknichrono.mgp.parser;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.model.out.MaxSpeed;
import org.teknichrono.mgp.model.rider.RiderDetails;
import org.teknichrono.mgp.model.rider.RiderSeason;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MaxSpeedPdfParser {
  private static final Logger LOGGER = Logger.getLogger(MaxSpeedPdfParser.class);


  public List<MaxSpeed> parse(String url, List<RiderDetails> ridersOfEvent, int year) throws PdfParsingException {
    List<MaxSpeed> toReturn = new ArrayList<>();
    String[] lines = PdfParserUtils.readPdfLines(url);
    boolean start = false;
    boolean end = false;
    for (String line : lines) {
      if (start && !Character.isDigit(line.charAt(0))) {
        end = true;
      }
      if (start && !end) {
        toReturn.add(parseLine(line, ridersOfEvent, year));
      }
      if (line.startsWith("Rider")) {
        start = true;
      }
    }
    if (!start || !end) {
      throw new PdfParsingException("Could not find the expected lines format in the PDF at " + url);
    }
    return toReturn;
  }

  MaxSpeed parseLine(String line, List<RiderDetails> ridersOfEvent, int year) throws PdfParsingException {
    MaxSpeed maxSpeed = new MaxSpeed();
    parseRider(line, maxSpeed, ridersOfEvent, year);
    maxSpeed.speed = PdfParserUtils.parseSpeed(line);
    maxSpeed.nation = PdfParserUtils.parseNation(line);
    if (maxSpeed.testIfIncomplete()) {
      throw new PdfParsingException("After parsing " + line + " all we could parse was: " + maxSpeed);
    }
    return maxSpeed;
  }

  private void parseRider(String line, MaxSpeed maxSpeed, List<RiderDetails> ridersOfEvent, int year) {
    for (RiderDetails rider : ridersOfEvent) {
      RiderSeason season = rider.getSeasonOfYear(year);
      String riderNumber = season.number.toString();
      if (line.startsWith(riderNumber)) {
        maxSpeed.number = season.number;
        maxSpeed.rider = rider.name + " " + rider.surname;
        maxSpeed.team = season.sponsored_team;
        maxSpeed.motorcycle = season.team.constructor.name;
      }
    }
  }

}
