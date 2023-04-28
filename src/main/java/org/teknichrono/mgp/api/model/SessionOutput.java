package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionOutput {

  public static final String TOPSPEED = "topspeed";
  private static final String TOPSPEED_DESCRIPTION = "";
  public static final String ANALYSIS = "analysis";
  private static final String ANALYSIS_DESCRIPTION = "";

  public String name;
  public List<Choice> additionalOptions;

  public String trackCondition;
  public String airTemperature;
  public String humidity;
  public String groundTemperature;
  public String weather;

  public String date;
  public String status;
  public SessionFilesOutput sessionFiles;
  public String circuit;

  protected SessionOutput() {
    this(false);
  }

  public SessionOutput(boolean withAdditionalOptions) {
    if (withAdditionalOptions) {
      additionalOptions = new ArrayList<>();
      additionalOptions.add(Choice.from(TOPSPEED, TOPSPEED_DESCRIPTION));
      additionalOptions.add(Choice.from(ANALYSIS, ANALYSIS_DESCRIPTION));
    }
  }


  protected void fillFromSession(Session s) {
    name = s.getSessionName();

    trackCondition = s.condition.track;
    airTemperature = s.condition.air;
    humidity = s.condition.humidity;
    groundTemperature = s.condition.ground;
    weather = s.condition.weather;

    date = s.date;
    status = s.status;
    sessionFiles = SessionFilesOutput.from(s.session_files);
    circuit = s.circuit;
  }
}
