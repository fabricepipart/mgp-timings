package org.teknichrono.mgp.model.out;

public class LapAnalysis {

  public static final String UNFINISHED_LAP = "unfinished";

  public LapAnalysis() {
    super();
  }

  public LapAnalysis(LapAnalysis old) {
    frontTyreLapNumber = old.frontTyreLapNumber + 1;
    backTyreLapNumber = old.backTyreLapNumber + 1;
    frontTyre = old.frontTyre;
    backTyre = old.backTyre;

    number = old.number;
    lapNumber = old.lapNumber;
    rider = old.rider;
    nation = old.nation;
    team = old.team;
    motorcycle = old.motorcycle;
  }

  public Integer number;
  public String rider;
  public String nation;
  public String team;
  public String motorcycle;

  public Integer lapNumber;
  public String time;
  public Float maxSpeed;

  public String frontTyre;
  public String backTyre;
  public Integer frontTyreLapNumber;
  public Integer backTyreLapNumber;

  public Boolean cancelled = Boolean.FALSE;
  public Boolean pit = Boolean.FALSE;
  public Boolean unfinished = Boolean.FALSE;

}
