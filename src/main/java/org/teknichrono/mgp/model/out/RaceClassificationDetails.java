package org.teknichrono.mgp.model.out;

import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.rider.RiderDetails;

import java.util.List;

public class RaceClassificationDetails extends ClassificationDetails {

  public Integer points;

  public String totalTime;

  public Float averageSpeed;


  public static RaceClassificationDetails from(Classification c, List<RiderDetails> ridersDetails, int year) {
    RaceClassificationDetails toReturn = new RaceClassificationDetails();
    toReturn.fill(c, ridersDetails, year);
    toReturn.points = getPointsForPosition(c.position);
    return toReturn;
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
