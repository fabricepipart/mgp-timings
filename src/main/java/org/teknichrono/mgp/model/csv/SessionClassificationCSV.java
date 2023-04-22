package org.teknichrono.mgp.model.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class SessionClassificationCSV {

  @CsvBindByName(column = "POSITION")
  @CsvBindByPosition(position = 0)
  public Integer position;

  @CsvBindByName(column = "NUMBER")
  @CsvBindByPosition(position = 1)
  public Integer riderNumber;

  @CsvBindByName(column = "POINTS")
  @CsvBindByPosition(position = 2)
  public Integer points;

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

  @CsvBindByName(column = "BEST_LAP")
  @CsvBindByPosition(position = 7)
  public String bestLapTime;

  @CsvBindByName(column = "BEST_LAP_NB")
  @CsvBindByPosition(position = 8)
  public Integer bestLapNumber;

  @CsvBindByName(column = "TOTAL_TIME")
  @CsvBindByPosition(position = 9)
  public String totalTime;

  @CsvBindByName(column = "TOTAL_LAPS")
  @CsvBindByPosition(position = 10)
  public Integer totalLaps;

  @CsvBindByName(column = "GAP_TO_FIRST")
  @CsvBindByPosition(position = 11)
  public Float gapToFirst;

  @CsvBindByName(column = "GAP_TO_PREV")
  @CsvBindByPosition(position = 12)
  public Float gapToPrevious;

  @CsvBindByName(column = "AVERAGE_SPEED")
  @CsvBindByPosition(position = 13)
  public Float averageSpeed;

  @CsvBindByName(column = "TOP_SPEED")
  @CsvBindByPosition(position = 14)
  public Float topSpeed;

}
