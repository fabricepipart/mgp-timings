package org.teknichrono.mgp.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.model.out.MaxSpeed;
import org.teknichrono.mgp.model.rider.RiderDetails;
import org.teknichrono.mgp.model.rider.RiderSeason;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class MaxSpeedPdfParser {
  private static final Logger LOGGER = Logger.getLogger(MaxSpeedPdfParser.class);


  public List<MaxSpeed> parse(String url, List<RiderDetails> ridersOfEvent, int year) throws PdfParsingException {
    List<MaxSpeed> toReturn = new ArrayList<>();
    try {
      InputStream in = new URL(url).openStream();
      try (PDDocument doc = PDDocument.load(in)) {
        PDFTextStripper tStripper = new PDFTextStripper();
        tStripper.setSortByPosition(true);
        String pdfFileInText = tStripper.getText(doc);
        // split by whitespace
        String lines[] = pdfFileInText.split("\\r?\\n");
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
      }
    } catch (IOException e) {
      throw new PdfParsingException("Error when reading the PDF at " + url, e);
    }
    return toReturn;
  }

  MaxSpeed parseLine(String line, List<RiderDetails> ridersOfEvent, int year) throws PdfParsingException {
    MaxSpeed maxSpeed = new MaxSpeed();
    parseRider(line, maxSpeed, ridersOfEvent, year);
    parseSpeed(line, maxSpeed);
    parseNation(line, maxSpeed);
    if (maxSpeed.testIfIncomplete()) {
      throw new PdfParsingException("After parsing " + line + " all we could parse was: " + maxSpeed);
    }
    return maxSpeed;
  }

  private void parseNation(String line, MaxSpeed maxSpeed) {
    String remaining = line;
    Pattern p = Pattern.compile("\\s[A-Z]{3}\\s");
    Matcher m = p.matcher(remaining);
    while (m.find()) {
      String nation = m.group().trim();
      maxSpeed.nation = nation;
      remaining = remaining.replaceFirst(nation, "");
    }
  }

  private void parseSpeed(String line, MaxSpeed maxSpeed) {
    String remaining = line;
    Pattern p = Pattern.compile("[0-9.]+$");
    Matcher m = p.matcher(remaining);
    while (m.find()) {
      String speedString = m.group();
      maxSpeed.speed = Float.parseFloat(speedString);
      remaining = remaining.replaceFirst(speedString, "");
    }
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
