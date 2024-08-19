package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Entry;
import org.teknichrono.mgp.client.model.rider.RiderAttributes;
import org.teknichrono.mgp.client.model.rider.RiderBiography;
import org.teknichrono.mgp.client.model.rider.RiderDetails;
import org.teknichrono.mgp.csv.converter.CSVConvertible;
import org.teknichrono.mgp.csv.model.SessionRiderCSV;

public class SessionRider implements CSVConvertible<SessionRiderCSV> {

  public Boolean wildcard;
  public Integer replacement;
  public Boolean replaced;
  public Boolean rookieOfTheYear;
  public Boolean independentTeamRider;

  public Integer number;
  public String team;
  public String constructor;

  public String name;
  public String surname;
  public String nickname;
  public CountryOutput country;
  public String birth_city;
  public String birth_date;
  public RiderAttributes physical_attributes;

  public RiderBiography biography;
  public Boolean legend;
  public Integer legacy_id;
  public String merchandise_url;
  public Boolean published;


  public void fill(Entry e) {
    this.wildcard = e.wildcard;
    this.replacement = e.replacement;
    this.replaced = e.replaced;
    this.rookieOfTheYear = e.rookieOfTheYear;
    this.independentTeamRider = e.independentTeamRider;

    this.number = e.rider.number;
    this.team = e.team.name;
    this.constructor = e.constructor.name;
    this.country = CountryOutput.from(e.rider.country);

    this.name = e.rider.full_name;
    this.surname = "";
  }

  public void fill(RiderDetails rider) {
    this.name = rider.name;
    this.surname = rider.surname;
    this.nickname = rider.nickname;
    this.country = CountryOutput.from(rider.country);
    this.birth_city = rider.birth_city;
    this.birth_date = rider.birth_date;
    this.physical_attributes = rider.physical_attributes;
    this.biography = rider.biography;
    this.legend = rider.legend;
    this.legacy_id = rider.legacy_id;
    this.merchandise_url = rider.merchandise_url;
    this.published = rider.published;
  }

  @Override
  public SessionRiderCSV toCsv() {
    return SessionRiderCSV.from(this);
  }
}
