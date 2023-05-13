package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.csv.model.EventCSV;

@ApplicationScoped
public class EventCsvConverter extends CsvConverter<Event, EventCSV> implements CsvConverterInterface {

  public Class<Event> inputClass() {
    return Event.class;
  }

  public Class<EventCSV> outputClass() {
    return EventCSV.class;
  }

}
