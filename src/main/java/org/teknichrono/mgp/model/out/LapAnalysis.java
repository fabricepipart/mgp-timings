package org.teknichrono.mgp.model.out;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class LapAnalysis {

  public static final String UNFINISHED_LAP = "unfinished";


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

  @CsvBindByName(column = "LAP_NUMBER")
  @CsvBindByPosition(position = 5)
  public Integer lapNumber;

  @CsvBindByName(column = "TIME")
  @CsvBindByPosition(position = 6)
  public String time;

  @CsvBindByName(column = "MAX_SPEED")
  @CsvBindByPosition(position = 7)
  public Float maxSpeed;

  @CsvBindByName(column = "FRONT_TYRE")
  @CsvBindByPosition(position = 8)
  public String frontTyre;

  @CsvBindByName(column = "BACK_TYRE")
  @CsvBindByPosition(position = 9)
  public String backTyre;

  @CsvBindByName(column = "FRONT_TYRE_LAP_NUMBER")
  @CsvBindByPosition(position = 10)
  public Integer frontTyreLapNumber;

  @CsvBindByName(column = "BACK_TYRE_LAP_NUMBER")
  @CsvBindByPosition(position = 11)
  public Integer backTyreLapNumber;

  @CsvBindByName(column = "CANCELLED")
  @CsvBindByPosition(position = 12)
  public Boolean cancelled = Boolean.FALSE;

  @CsvBindByName(column = "PIT")
  @CsvBindByPosition(position = 13)
  public Boolean pit = Boolean.FALSE;

  @CsvBindByName(column = "UNFINISHED")
  @CsvBindByPosition(position = 14)
  public Boolean unfinished = Boolean.FALSE;


  public LapAnalysis() {
    super();
  }

  public LapAnalysis(LapAnalysis old) {
    if (old.frontTyreLapNumber != null) {
      frontTyreLapNumber = old.frontTyreLapNumber + 1;
    }
    if (old.backTyreLapNumber != null) {
      backTyreLapNumber = old.backTyreLapNumber + 1;
    }
    frontTyre = old.frontTyre;
    backTyre = old.backTyre;

    number = old.number;
    lapNumber = old.lapNumber;
    rider = old.rider;
    nation = old.nation;
    team = old.team;
    motorcycle = old.motorcycle;
  }

}
