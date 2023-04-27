package org.teknichrono.mgp.model.output;

public class Choice {

  public String shortName;
  public String name;

  public static Choice from(String shortName, String name) {
    Choice c = new Choice();
    c.shortName = shortName;
    c.name = name;
    return c;
  }
}
