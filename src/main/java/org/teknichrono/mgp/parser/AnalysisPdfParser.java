package org.teknichrono.mgp.parser;

import org.jboss.logging.Logger;
import org.teknichrono.mgp.model.out.LapAnalysis;
import org.teknichrono.mgp.model.out.SessionRider;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class AnalysisPdfParser {

  private static final Logger LOGGER = Logger.getLogger(RaceResultsPdfParser.class);
  public static final int X_LEFT_COLUMN_PDF = 53;
  public static final int X_RIGHT_COLUMN_PDF = 314;
  public static final int T_COLUMNS_PDF = 40;
  public static final int WIDTH_COLUMN_PDF = 263;
  public static final int HEIGHT_COLUMN_PDF = 692;

  public List<LapAnalysis> parse(String url, List<SessionRider> ridersOfEvent) throws PdfParsingException {
    List<LapAnalysis> toReturn = new ArrayList<>();

    List<String> lines = PdfParserUtils.readPdfLinesTwoColumns(url, X_LEFT_COLUMN_PDF, X_RIGHT_COLUMN_PDF, T_COLUMNS_PDF, WIDTH_COLUMN_PDF, HEIGHT_COLUMN_PDF);

    LapAnalysis lapAnalysis = null;
    for (String line : lines) {
      lapAnalysis = updateRiderIfNecessary(lapAnalysis, ridersOfEvent, line);
      updateFrontTyreIfNecessary(lapAnalysis, line);
      updateRearTyreIfNecessary(lapAnalysis, line);
      updateFrontTyreAgeIfNecessary(lapAnalysis, line);
      updateRearTyreAgeIfNecessary(lapAnalysis, line);
      if (PdfParserUtils.startsWithNumber(line)) {
        lapAnalysis.lapNumber = Integer.parseInt(line.split(" ")[0]);
        lapAnalysis.pit = line.contains(" P ");
        lapAnalysis.unfinished = line.contains(LapAnalysis.UNFINISHED_LAP);
        lapAnalysis.cancelled = line.contains("*");
        lapAnalysis.maxSpeed = PdfParserUtils.parseSpeed(line);
        lapAnalysis.time = PdfParserUtils.parseTime(line);
        if (lapAnalysis.time != null) {
          toReturn.add(lapAnalysis);
          lapAnalysis = new LapAnalysis(lapAnalysis);
        }
      } else if (line.toLowerCase().contains(LapAnalysis.UNFINISHED_LAP.toLowerCase())) {
        lapAnalysis.lapNumber = lapAnalysis.lapNumber + 1;
        lapAnalysis.unfinished = true;
        lapAnalysis.cancelled = false;
        lapAnalysis.pit = false;
        lapAnalysis.maxSpeed = PdfParserUtils.parseSpeed(line);
        toReturn.add(lapAnalysis);
        lapAnalysis = new LapAnalysis(lapAnalysis);
      }
    }

    return toReturn;
  }

  private void updateFrontTyreAgeIfNecessary(LapAnalysis lap, String line) {
    int age = PdfParserUtils.parseFrontTyreAge(line);
    if (age >= 0) {
      lap.frontTyreLapNumber = age;
    }
  }

  private void updateRearTyreAgeIfNecessary(LapAnalysis lap, String line) {
    int age = PdfParserUtils.parseRearTyreAge(line);
    if (age >= 0) {
      lap.backTyreLapNumber = age;
    }
  }

  private void updateRearTyreIfNecessary(LapAnalysis lap, String line) {
    String s = PdfParserUtils.parseRearTyre(line);
    if (s != null) {
      lap.backTyre = s;
    }
  }

  private void updateFrontTyreIfNecessary(LapAnalysis lap, String line) {
    String s = PdfParserUtils.parseFrontTyre(line);
    if (s != null) {
      lap.frontTyre = s;
    }
  }

  private LapAnalysis updateRiderIfNecessary(LapAnalysis currentLapAnalysis, List<SessionRider> ridersOfEvent, String line) {
    LapAnalysis toReturn = currentLapAnalysis;
    for (SessionRider rider : ridersOfEvent) {
      Integer number = rider.season.number;
      String name = rider.name + " " + rider.surname;
      String motorcycle = rider.season.team.constructor.name;
      if (line.contains(number.toString()) &&
          line.toLowerCase().contains(name.toLowerCase()) &&
          line.toLowerCase(Locale.ROOT).contains(motorcycle.toLowerCase())) {
        toReturn = new LapAnalysis();
        toReturn.number = number;
        toReturn.motorcycle = motorcycle;
        toReturn.rider = name;
        toReturn.nation = PdfParserUtils.parseNation(line);
        toReturn.team = rider.season.sponsored_team;
      }
    }
    return toReturn;
  }
}
