package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.PdfFile;
import org.teknichrono.mgp.client.model.result.SessionFileType;

import java.util.Map;


public class SessionFilesOutput {

  public String analysisByLap;
  public String fastLapRider;
  public String combinedClassification;
  public String session;
  public String bestPartialTime;
  public String combinedPractice;
  public String classification;
  public String analysis;
  public String maximumSpeed;
  public String lapChart;
  public String grid;
  public String fastLapSequence;
  public String averageSpeed;
  public String worldStanding;

  public static SessionFilesOutput from(Map<SessionFileType, PdfFile> session_files) {
    SessionFilesOutput output = new SessionFilesOutput();
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.analysisByLap = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.fastLapRider = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.combinedClassification = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.session = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.bestPartialTime = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.combinedPractice = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.classification = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.analysis = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.maximumSpeed = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.lapChart = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.grid = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.fastLapSequence = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.averageSpeed = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
      output.worldStanding = session_files.get(SessionFileType.analysis_by_lap).url;
    }
    return output;
  }
}
