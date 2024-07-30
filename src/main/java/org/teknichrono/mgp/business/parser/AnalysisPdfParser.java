package org.teknichrono.mgp.business.parser;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.SectorTime;
import org.teknichrono.mgp.client.model.result.RiderClassification;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AnalysisPdfParser {

  public static final int X_LEFT_COLUMN_PDF = 53;
  public static final int X_RIGHT_COLUMN_PDF = 314;
  public static final int T_COLUMNS_PDF = 40;
  public static final int WIDTH_COLUMN_PDF = 263;
  public static final int HEIGHT_COLUMN_PDF = 692;

  public List<LapAnalysis> parse(String url, List<RiderClassification> riderClassifications) throws PdfParsingException {
    List<LapAnalysis> toReturn = new ArrayList<>();
    if (url != null) {
      List<String> lines = PdfParserUtils.readPdfLinesTwoColumns(url, X_LEFT_COLUMN_PDF, X_RIGHT_COLUMN_PDF, T_COLUMNS_PDF, WIDTH_COLUMN_PDF, HEIGHT_COLUMN_PDF);
      LapAnalysis lapAnalysis = null;
      for (String line : lines) {
        lapAnalysis = updateRiderIfNecessary(lapAnalysis, riderClassifications, line);
        updateFrontTyreIfNecessary(lapAnalysis, line);
        updateRearTyreIfNecessary(lapAnalysis, line);
        updateFrontTyreAgeIfNecessary(lapAnalysis, line);
        updateRearTyreAgeIfNecessary(lapAnalysis, line);
        lapAnalysis = updateLapInfoIfNecessary(toReturn, lapAnalysis, line);
      }
    }

    return toReturn;
  }

  private LapAnalysis updateLapInfoIfNecessary(List<LapAnalysis> toReturn, LapAnalysis lapAnalysis, String line) {
    if (lapAnalysis != null) {
      if (PdfParserUtils.startsWithNumber(line)) {
        lapAnalysis.lapNumber = Integer.parseInt(line.split(" ")[0]);
        lapAnalysis.pit = line.contains(" P ");
        lapAnalysis.unfinished = line.contains(LapAnalysis.UNFINISHED_LAP);
        lapAnalysis.cancelled = line.contains("*");
        lapAnalysis.maxSpeed = PdfParserUtils.parseSpeed(line);
        lapAnalysis.time = PdfParserUtils.parseTime(line);
        lapAnalysis.sectors = addSectors(line);
        lapAnalysis = resetLapAnalysisIfNecessary(toReturn, lapAnalysis);
      } else if (line.toLowerCase().contains(LapAnalysis.UNFINISHED_LAP.toLowerCase())) {
        lapAnalysis.lapNumber = lapAnalysis.lapNumber != null ? lapAnalysis.lapNumber + 1 : 1;
        lapAnalysis.unfinished = true;
        lapAnalysis.cancelled = false;
        lapAnalysis.pit = false;
        lapAnalysis.maxSpeed = PdfParserUtils.parseSpeed(line);
        toReturn.add(lapAnalysis);
        lapAnalysis = new LapAnalysis(lapAnalysis);
      }
    }
    return lapAnalysis;
  }

  private List<SectorTime> addSectors(String line) {
    List<String> times = PdfParserUtils.parseTimes(line);
    if (times.size() > 1) {
      times.remove(0);
    }
    List<SectorTime> sectors = new ArrayList<>();
    for (int i = 0; i < times.size(); i++) {
      SectorTime s = new SectorTime();
      s.sectorNumber = i + 1;
      s.time = times.get(i);
      sectors.add(s);
    }
    return sectors;
  }

  private LapAnalysis resetLapAnalysisIfNecessary(List<LapAnalysis> toReturn, LapAnalysis lapAnalysis) {
    if (lapAnalysis.time != null || lapAnalysis.pit || lapAnalysis.maxSpeed != null) {
      toReturn.add(lapAnalysis);
      lapAnalysis = new LapAnalysis(lapAnalysis);
    }
    return lapAnalysis;
  }

  private void updateFrontTyreAgeIfNecessary(LapAnalysis lap, String line) {
    int age = PdfParserUtils.parseFrontTyreAge(line);
    if (lap != null && age >= 0) {
      lap.frontTyreLapNumber = age;
    }
  }

  private void updateRearTyreAgeIfNecessary(LapAnalysis lap, String line) {
    int age = PdfParserUtils.parseRearTyreAge(line);
    if (lap != null && age >= 0) {
      lap.backTyreLapNumber = age;
    }
  }

  private void updateRearTyreIfNecessary(LapAnalysis lap, String line) {
    String s = PdfParserUtils.parseRearTyre(line);
    if (lap != null && s != null) {
      lap.backTyre = s;
    }
  }

  private void updateFrontTyreIfNecessary(LapAnalysis lap, String line) {
    String s = PdfParserUtils.parseFrontTyre(line);
    if (lap != null && s != null) {
      lap.frontTyre = s;
    }
  }

  private LapAnalysis updateRiderIfNecessary(LapAnalysis currentLapAnalysis, List<RiderClassification> riderClassifications, String line) {
    LapAnalysis toReturn = currentLapAnalysis;
    for (RiderClassification classification : riderClassifications) {
      Integer number = classification.rider.number;
      String name = classification.rider.full_name;
      String firstname = name.split(" ")[0];
      if (line.contains(number.toString()) &&
          line.toLowerCase().contains(firstname.toLowerCase())) {
        toReturn = new LapAnalysis();
        toReturn.number = number;
        toReturn.motorcycle = classification.constructor.name;
        toReturn.rider = name;
        toReturn.nation = classification.rider.country.iso;
        if (classification.team != null) {
          toReturn.team = classification.team.name;
        }
      }
    }
    return toReturn;
  }
}
