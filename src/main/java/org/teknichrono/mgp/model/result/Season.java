package org.teknichrono.mgp.model.result;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class Season {

  @CsvBindByName(column = "YEAR")
  @CsvBindByPosition(position = 0)
  public String id;

  @CsvBindByName(column = "ID")
  @CsvBindByPosition(position = 1)
  public Integer year;

  @CsvBindByName(column = "CURRENT")
  @CsvBindByPosition(position = 2)
  public boolean current;

}
