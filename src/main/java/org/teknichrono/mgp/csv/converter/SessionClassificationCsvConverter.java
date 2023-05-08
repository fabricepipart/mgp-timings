package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.csv.model.SessionClassificationCSV;

@ApplicationScoped
public class SessionClassificationCsvConverter extends CsvConverter<SessionClassificationOutput, SessionClassificationCSV> implements CsvConverterInterface {

  public Class<SessionClassificationOutput> inputClass() {
    return SessionClassificationOutput.class;
  }

  public Class<SessionClassificationCSV> outputClass() {
    return SessionClassificationCSV.class;
  }

}
