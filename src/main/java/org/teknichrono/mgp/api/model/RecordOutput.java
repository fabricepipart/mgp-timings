package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Record;

public class RecordOutput {

  public RiderOutput rider;
  public String type;
  public String bestLap;
  public Float speed;
  public Integer year;

  public static RecordOutput from(Record r) {
    RecordOutput recordOutput = new RecordOutput();
    recordOutput.type = r.type;
    recordOutput.rider = RiderOutput.from(r.rider);
    recordOutput.bestLap = r.bestLap.time;
    recordOutput.speed = r.speed;
    recordOutput.year = r.year;
    return recordOutput;
  }
}
