package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.api.model.MaxSpeed;

@ApplicationScoped
public class MaxSpeedCsvConverter extends CsvConverter<MaxSpeed, MaxSpeed> implements CsvConverterInterface {

  public Class<MaxSpeed> inputClass() {
    return MaxSpeed.class;
  }

  public Class<MaxSpeed> outputClass() {
    return MaxSpeed.class;
  }

}
