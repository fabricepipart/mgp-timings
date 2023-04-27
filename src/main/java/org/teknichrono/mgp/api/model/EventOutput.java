package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Event;
import org.teknichrono.mgp.client.model.result.PdfFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventOutput {

  public String name;
  public String sponsored_name;
  public String date_start;
  public String date_end;
  public String countryName;
  public String circuitName;
  public Map<String, String> eventFilesUrls = new HashMap<>();
  public boolean test;
  public String short_name;
  public Map<String, String> worldStandingFilesUrls;
  public List<Choice> categories = new ArrayList<>();

  public static EventOutput from(Event e) {
    EventOutput event = new EventOutput();
    event.name = e.name;
    event.sponsored_name = e.sponsored_name;
    event.date_start = e.date_start;
    event.date_end = e.date_end;
    event.countryName = String.format("%s (%s)", e.country.name, e.country.iso);
    event.circuitName = e.circuit.name;
    for (Map.Entry<String, PdfFile> entry : e.event_files.entrySet()) {
      event.eventFilesUrls.put(entry.getKey(), entry.getValue().url);
    }
    event.test = e.test;
    event.short_name = e.short_name;
    if (e.world_standing_files != null) {
      for (Map.Entry<String, PdfFile> entry : e.world_standing_files.entrySet()) {
        event.worldStandingFilesUrls.put(entry.getKey(), entry.getValue().url);
      }
    }
    return event;
  }
}
