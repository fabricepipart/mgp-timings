package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.api.model.SessionRider;
import org.teknichrono.mgp.csv.model.SessionRiderCSV;

@ApplicationScoped
public class SessionRiderCsvConverter extends CsvConverter<SessionRider, SessionRiderCSV> implements CsvConverterInterface {

  public Class<SessionRider> inputClass() {
    return SessionRider.class;
  }

  public Class<SessionRiderCSV> outputClass() {
    return SessionRiderCSV.class;
  }

}
