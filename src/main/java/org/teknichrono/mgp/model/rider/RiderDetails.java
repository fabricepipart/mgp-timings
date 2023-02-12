package org.teknichrono.mgp.model.rider;

import java.util.List;
import java.util.stream.Collectors;

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

  public RiderSeason getSeasonOfYear(String teamName, int year) {
    // Only one for that year
    List<RiderSeason> seasonsOfSameYear = career.stream().filter(s -> s.season == year).collect(Collectors.toList());
    if (seasonsOfSameYear != null && seasonsOfSameYear.size() == 1) {
      return seasonsOfSameYear.get(0);
    } else {
      for (RiderSeason riderSeason : seasonsOfSameYear) {
        if (riderSeason.team.name.equalsIgnoreCase(teamName) || riderSeason.sponsored_team.equalsIgnoreCase(teamName)) {
          return riderSeason;
        }
      }
    }
    // Still no match?
    if (seasonsOfSameYear != null && !seasonsOfSameYear.isEmpty()) {
      return seasonsOfSameYear.get(0);
    }
    return null;
  }

  public String fullName() {
    return name + " " + surname;
  }

}
