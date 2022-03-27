package org.teknichrono.mgp.model.rider;

import java.util.List;

public class RiderDetails {
  public String id;
  public String name;
  public String surname;
  public String nickname;
  public RiderCountry country;
  public String birth_city;
  public String birth_date;
  public RiderAttributes physical_attributes;
  public List<RiderSeason> career;
  public RiderBiography biography;
  public Boolean legend;
  public Integer legacy_id;
  public String merchandise_url;
  public Boolean published;
  public Boolean injured;
  public Boolean banned;
  public Boolean wildcard;

  public RiderSeason getSeasonOfYear(int year) {
    return career.stream().filter(s -> s.season == year).findFirst().get();
  }

  public String riderFullName() {
    return name + " " + surname;
  }

}
