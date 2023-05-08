package org.teknichrono.mgp.csv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.api.model.SessionRider;

public class SessionRiderCSV {

  @CsvBindByName(column = "NAME")
  @CsvBindByPosition(position = 0)
  public String name;
  @CsvBindByName(column = "SURNAME")
  @CsvBindByPosition(position = 1)
  public String surname;
  @CsvBindByName(column = "NICKNAME")
  @CsvBindByPosition(position = 2)
  public String nickname;
  @CsvBindByName(column = "COUNTRY")
  @CsvBindByPosition(position = 3)
  public String country;
  @CsvBindByName(column = "BIRTH_CITY")
  @CsvBindByPosition(position = 4)
  public String birthCity;
  @CsvBindByName(column = "BIRTH_DATE")
  @CsvBindByPosition(position = 5)
  public String birthDate;


  @CsvBindByName(column = "WILDCARD")
  @CsvBindByPosition(position = 6)
  public Boolean wildcard;
  @CsvBindByName(column = "REPLACEMENT")
  @CsvBindByPosition(position = 7)
  public Integer replacement;
  @CsvBindByName(column = "REPLACED")
  @CsvBindByPosition(position = 8)
  public Boolean replaced;
  @CsvBindByName(column = "ROOKIEOFTHEYEAR")
  @CsvBindByPosition(position = 9)
  public Boolean rookieOfTheYear;
  @CsvBindByName(column = "INDEPENDENTTEAMRIDER")
  @CsvBindByPosition(position = 10)
  public Boolean independentTeamRider;


  public static SessionRiderCSV from(SessionRider sessionRider) {
    SessionRiderCSV csv = new SessionRiderCSV();
    csv.name = sessionRider.name;
    csv.surname = sessionRider.surname;
    csv.nickname = sessionRider.nickname;
    csv.country = sessionRider.country.iso;
    csv.birthCity = sessionRider.birth_city;
    csv.birthDate = sessionRider.birth_date;
    csv.wildcard = sessionRider.wildcard;
    csv.replacement = sessionRider.replacement;
    csv.replaced = sessionRider.replaced;
    csv.rookieOfTheYear = sessionRider.rookieOfTheYear;
    csv.independentTeamRider = sessionRider.independentTeamRider;
    return csv;
  }
}
