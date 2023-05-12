package org.teknichrono.mgp.client.model.result;

import org.teknichrono.mgp.csv.model.EventCSV;
import org.teknichrono.mgp.csv.converter.CSVConvertible;

import java.util.List;
import java.util.Map;

public class Event implements CSVConvertible<EventCSV> {

  public String id;
  public String name;
  public String sponsored_name;
  public String date_start;
  public String date_end;
  public Country country;
  public List<LegacyCategory> legacy_id;
  public Circuit circuit;
  public Map<String, PdfFile> event_files;
  public boolean test;
  public String short_name;

  @Override
  public EventCSV toCsv() {
    return EventCSV.from(this);
  }
}
