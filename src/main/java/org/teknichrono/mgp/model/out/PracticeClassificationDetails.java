package org.teknichrono.mgp.model.out;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.model.result.RiderClassification;
import org.teknichrono.mgp.model.rider.RiderDetails;
import org.teknichrono.mgp.model.rider.RiderSeason;

import java.util.List;

public class PracticeClassificationDetails implements ClassificationDetails {

  @CsvBindByName(column = "POSITION")
  @CsvBindByPosition(position = 0)
  public Integer position;

  @CsvBindByName(column = "NUMBER")
  @CsvBindByPosition(position = 1)
  public Integer riderNumber;

  @CsvBindByName(column = "NAME")
  @CsvBindByPosition(position = 2)
  public String riderName;

  @CsvBindByName(column = "NATION")
  @CsvBindByPosition(position = 3)
  public String nation;

  @CsvBindByName(column = "TEAM")
  @CsvBindByPosition(position = 4)
  public String team;

  @CsvBindByName(column = "CONSTRUCTOR")
  @CsvBindByPosition(position = 5)
  public String constructor;

  @CsvBindByName(column = "BEST_LAP")
  @CsvBindByPosition(position = 6)
  public String bestLapTime;

  @CsvBindByName(column = "BEST_LAP_NB")
  @CsvBindByPosition(position = 7)
  public Integer bestLapNumber;

  @CsvBindByName(column = "TOTAL_LAPS")
  @CsvBindByPosition(position = 8)
  public Integer totalLaps;

  @CsvBindByName(column = "GAP_TO_FIRST")
  @CsvBindByPosition(position = 9)
  public Float gapToFirst;

  @CsvBindByName(column = "GAP_TO_PREV")
  @CsvBindByPosition(position = 10)
  public Float gapToPrevious;

  @CsvBindByName(column = "TOP_SPEED")
  @CsvBindByPosition(position = 11)
  public Float topSpeed;

  public static PracticeClassificationDetails from(RiderClassification c, List<RiderDetails> ridersDetails, int year) {
    PracticeClassificationDetails toReturn = new PracticeClassificationDetails();
    toReturn.fill(c, ridersDetails, year);
    return toReturn;
  }

  public void fill(RiderClassification c, List<RiderDetails> ridersDetails, int year) {
    position = c.position;
    riderName = c.rider.full_name;
    for (RiderDetails details : ridersDetails) {
      if (c.rider.full_name.equalsIgnoreCase(details.fullName())) {
        RiderSeason season = details.getSeasonOfYear(c.team.name, year);
        riderNumber = season.number;
        team = season.sponsored_team;
        if (season.team != null && season.team.constructor != null) {
          constructor = season.team.constructor.name;
        }
      }
    }
    gapToFirst = c.gap.first;
    gapToPrevious = c.gap.prev;
    totalLaps = c.total_laps;
  }
}
