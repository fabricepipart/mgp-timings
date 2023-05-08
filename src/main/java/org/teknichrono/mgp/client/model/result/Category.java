package org.teknichrono.mgp.client.model.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.teknichrono.mgp.csv.converter.CSVConvertible;
import org.teknichrono.mgp.csv.model.CategoryCSV;

public class Category implements CSVConvertible<CategoryCSV> {

  public String id;
  public String name;
  public Integer legacyId;

  @Override
  public CategoryCSV toCsv() {
    return CategoryCSV.from(this);
  }

  @JsonIgnore
  public String getShortName() {
    return name.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
  }

}
