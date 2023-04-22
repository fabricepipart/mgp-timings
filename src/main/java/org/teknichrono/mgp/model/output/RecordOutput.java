package org.teknichrono.mgp.model.output;

import org.teknichrono.mgp.api.model.result.Record;

public class RecordOutput {

  public RiderOutput rider;
  public String type;
  public String bestLap;
  public Float speed;
  public Integer year;

  public static RecordOutput from(Record r) {
    RecordOutput record = new RecordOutput();
    record.type = r.type;
    record.rider = RiderOutput.from(r.rider);
    record.bestLap = r.bestLap.time;
    record.speed = r.speed;
    record.year = r.year;
    return record;
  }
}
