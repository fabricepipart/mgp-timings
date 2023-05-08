package org.teknichrono.mgp.client.model.result;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.csv.model.SeasonCSV;
import org.teknichrono.mgp.csv.converter.CSVConvertible;

public class Season implements CSVConvertible<SeasonCSV> {

  @CsvBindByName(column = "YEAR")
  @CsvBindByPosition(position = 0)
  public String id;

  @CsvBindByName(column = "ID")
  @CsvBindByPosition(position = 1)
  public Integer year;

  @CsvBindByName(column = "CURRENT")
  @CsvBindByPosition(position = 2)
  public boolean current;

  @Override
  public SeasonCSV toCsv() {
    return SeasonCSV.from(this);
  }
}
