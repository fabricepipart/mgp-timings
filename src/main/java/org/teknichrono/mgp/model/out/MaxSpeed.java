package org.teknichrono.mgp.model.out;

public class MaxSpeed {

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
}
