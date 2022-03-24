package org.teknichrono.mgp.model.out;

import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.rider.RiderDetails;
import org.teknichrono.mgp.model.rider.RiderSeason;

import java.util.List;

public class ClassificationDetails {

  public Integer position;

  public Integer riderNumber;

  public String riderName;

  public String nation;

  public String team;

  public String constructor;

  public Integer totalLaps;

  public Float gapToFirst;

  protected void fill(Classification c, List<RiderDetails> ridersDetails, int year) {
    position = c.position;
    riderName = c.rider.full_name;
    for (RiderDetails details : ridersDetails) {
      if (c.rider.full_name.equalsIgnoreCase(details.riderFullName())) {
        RiderSeason season = details.getSeasonOfYear(year);
        riderNumber = season.number;
        team = season.sponsored_team;
        constructor = season.team.constructor.name;
      }
      gapToFirst = c.gap.first;
      totalLaps = c.total_laps;
    }
  }

}
