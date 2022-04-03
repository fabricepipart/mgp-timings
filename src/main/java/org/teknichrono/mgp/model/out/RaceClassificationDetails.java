package org.teknichrono.mgp.model.out;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.rider.RiderSeason;

import java.util.List;

public class RaceClassificationDetails implements ClassificationDetails {

  @CsvBindByName(column = "POSITION")
  @CsvBindByPosition(position = 0)
  public Integer position;

  @CsvBindByName(column = "POINTS")
  @CsvBindByPosition(position = 1)
  public Integer points;

  @CsvBindByName(column = "NUMBER")
  @CsvBindByPosition(position = 2)
  public Integer riderNumber;

  @CsvBindByName(column = "NAME")
  @CsvBindByPosition(position = 3)
  public String riderName;

  @CsvBindByName(column = "NATION")
  @CsvBindByPosition(position = 4)
  public String nation;

  @CsvBindByName(column = "TEAM")
  @CsvBindByPosition(position = 5)
  public String team;

  @CsvBindByName(column = "CONSTRUCTOR")
  @CsvBindByPosition(position = 6)
  public String constructor;

  @CsvBindByName(column = "TOTAL_TIME")
  @CsvBindByPosition(position = 7)
  public String totalTime;

  @CsvBindByName(column = "TOTAL_LAPS")
  @CsvBindByPosition(position = 8)
  public Integer totalLaps;

  @CsvBindByName(column = "GAP_TO_FIRST")
  @CsvBindByPosition(position = 9)
  public Float gapToFirst;

  @CsvBindByName(column = "AVERAGE_SPEED")
  @CsvBindByPosition(position = 10)
  public Float averageSpeed;


  public static RaceClassificationDetails from(Classification c, List<SessionRider> ridersDetails) {
    RaceClassificationDetails toReturn = new RaceClassificationDetails();
    toReturn.fill(c, ridersDetails);
    return toReturn;
  }

  public void fill(Classification c, List<SessionRider> ridersDetails) {
    position = c.position;
    riderName = c.rider.full_name;
    for (SessionRider details : ridersDetails) {
      if (c.rider.full_name.equalsIgnoreCase(details.fullName())) {
        RiderSeason season = details.season;
        riderNumber = season.number;
        team = season.sponsored_team;
        constructor = season.team.constructor.name;
      }
    }
    gapToFirst = c.gap.first;
    totalLaps = c.total_laps;
    points = getPointsForPosition(c.position);
  }

  private static Integer getPointsForPosition(Integer position) {
    if (position != null) {
      switch (position) {
        case 1:
          return 25;
        case 2:
          return 20;
        case 3:
          return 16;
        case 4:
          return 14;
        case 5:
          return 12;
        case 6:
          return 10;
        case 7:
          return 9;
        case 8:
          return 8;
        case 9:
          return 7;
        case 10:
          return 6;
        case 11:
          return 5;
        case 12:
          return 4;
        case 13:
          return 3;
        case 14:
          return 2;
        case 15:
          return 1;
        default:
          return 0;
      }
    }
    return null;
  }
}
