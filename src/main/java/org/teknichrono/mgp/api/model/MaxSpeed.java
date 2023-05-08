package org.teknichrono.mgp.api.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.csv.converter.CSVConvertible;

public class MaxSpeed implements CSVConvertible<MaxSpeed> {
  
  @CsvBindByName(column = "NUMBER")
  @CsvBindByPosition(position = 0)
  public Integer number;
  @CsvBindByName(column = "RIDER")
  @CsvBindByPosition(position = 1)
  public String rider;
  @CsvBindByName(column = "NATION")
  @CsvBindByPosition(position = 2)
  public String nation;
  @CsvBindByName(column = "TEAM")
  @CsvBindByPosition(position = 3)
  public String team;
  @CsvBindByName(column = "MOTORCYCLE")
  @CsvBindByPosition(position = 4)
  public String motorcycle;
  @CsvBindByName(column = "SPEED")
  @CsvBindByPosition(position = 5)
  public Float speed;

  public boolean testIfIncomplete() {
    return number == null || rider == null || nation == null || team == null || motorcycle == null || speed == null;
  }

  public String toString() {
    return String.format("[number=%d,rider=%s,nation=%s,team=%s,motorcycle=%s,speed=%f]", number, rider, nation, team, motorcycle, speed);
  }

  @Override
  public MaxSpeed toCsv() {
    return this;
  }
}
