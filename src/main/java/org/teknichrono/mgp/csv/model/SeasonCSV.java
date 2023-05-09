package org.teknichrono.mgp.csv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.client.model.result.Season;

public class SeasonCSV {

  @CsvBindByName(column = "YEAR")
  @CsvBindByPosition(position = 0)
  public Integer year;

  @CsvBindByName(column = "CURRENT")
  @CsvBindByPosition(position = 1)
  public boolean current;

  public static SeasonCSV from(Season season) {
    SeasonCSV csv = new SeasonCSV();
    csv.year = season.year;
    csv.current = season.current;
    return csv;
  }

}
