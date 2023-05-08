package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.csv.converter.CSVConvertible;
import org.teknichrono.mgp.csv.model.MaxSpeedCSV;

public class MaxSpeed implements CSVConvertible<MaxSpeedCSV> {

  public Integer number;
  public String rider;
  public String nation;
  public String team;
  public String motorcycle;
  public Float speed;

  public boolean testIfIncomplete() {
    return number == null || rider == null || nation == null || team == null || motorcycle == null || speed == null;
  }

  public String toString() {
    return String.format("[number=%d,rider=%s,nation=%s,team=%s,motorcycle=%s,speed=%f]", number, rider, nation, team, motorcycle, speed);
  }

  @Override
  public MaxSpeedCSV toCsv() {
    return MaxSpeedCSV.from(this);
  }
}
