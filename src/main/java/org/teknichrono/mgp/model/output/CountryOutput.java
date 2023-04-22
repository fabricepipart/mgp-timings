package org.teknichrono.mgp.model.output;

import org.teknichrono.mgp.api.model.result.Country;

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
}
