package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Country;
import org.teknichrono.mgp.client.model.rider.RiderCountry;

public class CountryOutput {

  public String name;
  public String iso;
  public String regionIso;

  public static CountryOutput from(Country c) {
    CountryOutput country = new CountryOutput();
    country.name = c.name;
    country.iso = c.iso;
    country.regionIso = c.region_iso;
    return country;
  }

  public static CountryOutput from(String nation) {
    CountryOutput country = new CountryOutput();
    country.iso = nation;
    return country;
  }

  public static CountryOutput from(RiderCountry c) {
    CountryOutput country = new CountryOutput();
    country.name = c.name;
    country.iso = c.iso;
    return country;
  }
}
