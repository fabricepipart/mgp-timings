package org.teknichrono.mgp.csv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.client.model.result.Event;

public class EventCSV {

  @CsvBindByName(column = "NAME")
  @CsvBindByPosition(position = 0)
  public String name;
  @CsvBindByName(column = "SPONSORED_NAME")
  @CsvBindByPosition(position = 1)
  public String sponsoredName;
  @CsvBindByName(column = "SHORT_NAME")
  @CsvBindByPosition(position = 2)
  public String shortName;
  @CsvBindByName(column = "TEST")
  @CsvBindByPosition(position = 3)
  public boolean test;
  @CsvBindByName(column = "DATE_START")
  @CsvBindByPosition(position = 4)
  public String dateStart;
  @CsvBindByName(column = "DATE_END")
  @CsvBindByPosition(position = 5)
  public String dateEnd;
  @CsvBindByName(column = "CIRCUIT")
  @CsvBindByPosition(position = 6)
  public String circuit;
  @CsvBindByName(column = "COUNTRY")
  @CsvBindByPosition(position = 7)
  public String country;


  public static EventCSV from(Event event) {
    EventCSV csv = new EventCSV();
    csv.name = event.name;
    csv.test = event.test;
    csv.sponsoredName = event.sponsored_name;
    csv.dateStart = event.date_start;
    csv.country = event.country.iso;
    csv.circuit = event.circuit.name;
    csv.shortName = event.short_name;
    return csv;
  }
}
