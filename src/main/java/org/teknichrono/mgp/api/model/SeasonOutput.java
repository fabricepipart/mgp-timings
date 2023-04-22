package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Season;

import java.util.ArrayList;
import java.util.List;

public class SeasonOutput {

  public Integer year;
  public boolean current;
  public List<Choice> races = new ArrayList<>();
  public List<Choice> tests = new ArrayList<>();

  public static SeasonOutput from(Season s) {
    SeasonOutput y = new SeasonOutput();
    y.year = s.year;
    y.current = s.current;
    return y;
  }
}
