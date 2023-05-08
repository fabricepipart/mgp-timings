package org.teknichrono.mgp.csv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.client.model.result.Category;

public class CategoryCSV {

  @CsvBindByName(column = "NAME")
  @CsvBindByPosition(position = 0)
  public String name;

  @CsvBindByName(column = "SHORT_NAME")
  @CsvBindByPosition(position = 1)
  public String shortName;

  public static CategoryCSV from(Category category) {
    CategoryCSV csv = new CategoryCSV();
    csv.name = category.name;
    csv.shortName = category.getShortName();
    return csv;
  }
}
