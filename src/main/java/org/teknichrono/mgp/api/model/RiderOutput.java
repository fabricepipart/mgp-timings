package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Rider;

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

  public static RiderOutput from(SessionRider r) {
    RiderOutput rider = new RiderOutput();
    rider.full_name = r.name + " " + r.surname;
    rider.country = r.country;
    rider.number = r.number;
    return rider;
  }
}
