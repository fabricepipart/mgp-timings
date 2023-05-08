package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.api.model.MaxSpeed;
import org.teknichrono.mgp.csv.model.MaxSpeedCSV;

@ApplicationScoped
public class MaxSpeedCsvConverter extends CsvConverter<MaxSpeed, MaxSpeedCSV> implements CsvConverterInterface {

  public Class<MaxSpeed> inputClass() {
    return MaxSpeed.class;
  }

  public Class<MaxSpeedCSV> outputClass() {
    return MaxSpeedCSV.class;
  }

}
