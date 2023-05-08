package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.csv.model.RiderClassificationCSV;

@ApplicationScoped
public class RiderClassificationCsvConverter extends CsvConverter<RiderClassification, RiderClassificationCSV> implements CsvConverterInterface {

  public Class<RiderClassification> inputClass() {
    return RiderClassification.class;
  }

  public Class<RiderClassificationCSV> outputClass() {
    return RiderClassificationCSV.class;
  }

}
