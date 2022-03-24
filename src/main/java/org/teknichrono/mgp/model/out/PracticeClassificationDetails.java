package org.teknichrono.mgp.model.out;

import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.rider.RiderDetails;

import java.util.List;

public class PracticeClassificationDetails extends ClassificationDetails {

  public String bestLapTime;

  public Integer bestLapNumber;

  public Float gapToPrevious;

  public Float topSpeed;

  public static PracticeClassificationDetails from(Classification c, List<RiderDetails> ridersDetails, int year) {
    PracticeClassificationDetails toReturn = new PracticeClassificationDetails();
    toReturn.fill(c, ridersDetails, year);
    return toReturn;
  }
}
