package org.teknichrono.model.client;

import java.util.List;
import java.util.Map;

public class Event {

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
  public Map<String, PdfFile> world_standing_files;
}
