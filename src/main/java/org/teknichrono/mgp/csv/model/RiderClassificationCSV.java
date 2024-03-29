package org.teknichrono.mgp.csv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.client.model.result.Constructor;
import org.teknichrono.mgp.client.model.result.Gap;
import org.teknichrono.mgp.client.model.result.Lap;
import org.teknichrono.mgp.client.model.result.Rider;
import org.teknichrono.mgp.client.model.result.Team;

public class RiderClassificationCSV {

  public String id;

  @CsvBindByName(column = "POSITION")
  @CsvBindByPosition(position = 0)
  public Integer position;

  @CsvBindByName(column = "RIDER")
  @CsvBindByPosition(position = 1)
  public Rider rider;

  @CsvBindByName(column = "TEAM")
  @CsvBindByPosition(position = 2)
  public Team team;

  @CsvBindByName(column = "CONSTRUCTOR")
  @CsvBindByPosition(position = 3)
  public Constructor constructor;

  @CsvBindByName(column = "BEST_LAP")
  @CsvBindByPosition(position = 3)
  public Lap best_lap;

  @CsvBindByName(column = "TOTAL_LAPS")
  @CsvBindByPosition(position = 4)
  public Integer total_laps;

  @CsvBindByPosition(position = 5)
  public String status;

  @CsvBindByName(column = "TOP_SPEED")
  @CsvBindByPosition(position = 6)
  public Float top_speed;

  @CsvBindByName(column = "GAP")
  @CsvBindByPosition(position = 7)
  public Gap gap;
}
