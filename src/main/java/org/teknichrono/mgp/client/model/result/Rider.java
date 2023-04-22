package org.teknichrono.mgp.client.model.result;

public class Rider {

  public String id;
  public String full_name;
  public Country country;
  public Integer legacy_id;
  public Integer number;

  public String toString() {
    return full_name;
  }

}
