package org.teknichrono.model.client;

public class Rider {

  public String id;
  public String full_name;
  public Country country;
  public Integer legacy_id;

  public String toString() {
    return full_name;
  }

}
