package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.csv.model.SessionCSV;

@ApplicationScoped
public class SessionCsvConverter extends CsvConverter<Session, SessionCSV> implements CsvConverterInterface {

  public Class<Session> inputClass() {
    return Session.class;
  }

  public Class<SessionCSV> outputClass() {
    return SessionCSV.class;
  }

}
