package org.teknichrono.mgp.model.output;

import org.teknichrono.mgp.api.model.result.Rider;

public class RiderOutput {

  public String full_name;
  public CountryOutput country;
  public Integer number;

  public static RiderOutput from(Rider r) {
    RiderOutput rider = new RiderOutput();
    rider.full_name = r.full_name;
    rider.country = CountryOutput.from(r.country);
    rider.number = r.number;
    return rider;
  }

  public static RiderOutput from(Integer riderNumber, String riderName, String nation) {
    RiderOutput rider = new RiderOutput();
    rider.full_name = riderName;
    rider.country = CountryOutput.from(nation);
    rider.number = riderNumber;
    return rider;
  }

  public String toString() {
    return full_name;
  }
}
