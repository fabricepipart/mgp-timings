package org.teknichrono.mgp.api.model.result;

import org.teknichrono.mgp.model.csv.RiderClassificationCSV;
import org.teknichrono.mgp.util.CSVConvertible;

public class RiderClassification implements CSVConvertible<RiderClassificationCSV> {

  public String id;
  public Integer position;
  public Integer points;
  public Rider rider;
  public Team team;
  public Constructor constructor;
  public Lap best_lap;
  public Integer total_laps;
  public String time;
  public String status;
  public Float average_speed;
  public Float top_speed;
  public Gap gap;

  @Override
  public RiderClassificationCSV toCsv() {
    RiderClassificationCSV toReturn = new RiderClassificationCSV();

    toReturn.id = id;
    toReturn.position = position;
    toReturn.rider = rider;
    toReturn.team = team;
    toReturn.constructor = constructor;
    toReturn.best_lap = best_lap;
    toReturn.total_laps = total_laps;
    toReturn.status = status;
    toReturn.top_speed = top_speed;
    toReturn.gap = gap;

    return toReturn;
  }
}