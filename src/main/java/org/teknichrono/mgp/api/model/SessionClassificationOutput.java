package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.csv.model.SessionClassificationCSV;
import org.teknichrono.mgp.csv.util.CSVConvertible;

public class SessionClassificationOutput implements CSVConvertible<SessionClassificationCSV> {

  public Integer position;
  public RiderOutput rider;
  public String team;
  public String constructor;
  public String bestLapTime;
  public Integer bestLapNumber;
  public Integer totalLaps;
  public String status;
  public Float topSpeed;
  public Float averageSpeed;
  public Float gapToFirst;
  private int lapsToFirst;
  public Float gapToPrevious;
  public Integer points;
  public String totalTime;

  public static SessionClassificationOutput from(RiderClassification classification) {
    SessionClassificationOutput output = new SessionClassificationOutput();
    output.position = classification.position;
    output.points = classification.points;
    output.rider = RiderOutput.from(classification.rider);
    output.team = classification.team != null ? classification.team.name : null;
    output.constructor = classification.constructor != null ? classification.constructor.name : null;
    if (classification.best_lap != null) {
      output.bestLapTime = classification.best_lap.time;
      output.bestLapNumber = classification.best_lap.number;
    }
    output.totalLaps = classification.total_laps;
    output.status = classification.status;
    output.topSpeed = classification.top_speed;
    output.averageSpeed = classification.average_speed;
    if (classification.gap != null) {
      output.gapToFirst = classification.gap.first;
      if (classification.gap.lap != null) {
        output.lapsToFirst = Integer.parseInt(classification.gap.lap);
      }
      output.gapToPrevious = classification.gap.prev;
    }
    output.totalTime = classification.time;
    return output;
  }


  public SessionClassificationCSV toCsv() {
    SessionClassificationCSV output = new SessionClassificationCSV();

    output.position = this.position;
    output.points = this.points;
    output.riderName = this.rider.full_name;
    output.riderNumber = this.rider.number;
    output.nation = this.rider.country.iso;
    output.team = this.team;
    output.constructor = this.constructor;
    output.bestLapTime = this.bestLapTime;
    output.bestLapNumber = this.bestLapNumber;
    output.totalTime = this.totalTime;
    output.totalLaps = this.totalLaps;
    output.gapToFirst = this.gapToFirst;
    output.gapToPrevious = this.gapToPrevious;
    output.averageSpeed = this.averageSpeed;
    output.topSpeed = this.topSpeed;

    return output;
  }
}
