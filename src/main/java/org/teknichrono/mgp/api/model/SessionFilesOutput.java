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
    if (session_files != null) {
      if (session_files.containsKey(SessionFileType.analysis_by_lap)) {
        output.analysisByLap = session_files.get(SessionFileType.analysis_by_lap).url;
      }
      if (session_files.containsKey(SessionFileType.fast_lap_rider)) {
        output.fastLapRider = session_files.get(SessionFileType.fast_lap_rider).url;
      }
      if (session_files.containsKey(SessionFileType.combined_classification)) {
        output.combinedClassification = session_files.get(SessionFileType.combined_classification).url;
      }
      if (session_files.containsKey(SessionFileType.session)) {
        output.session = session_files.get(SessionFileType.session).url;
      }
      if (session_files.containsKey(SessionFileType.best_partial_time)) {
        output.bestPartialTime = session_files.get(SessionFileType.best_partial_time).url;
      }
      if (session_files.containsKey(SessionFileType.combined_practice)) {
        output.combinedPractice = session_files.get(SessionFileType.combined_practice).url;
      }
      if (session_files.containsKey(SessionFileType.classification)) {
        output.classification = session_files.get(SessionFileType.classification).url;
      }
      if (session_files.containsKey(SessionFileType.analysis)) {
        output.analysis = session_files.get(SessionFileType.analysis).url;
      }
      if (session_files.containsKey(SessionFileType.maximum_speed)) {
        output.maximumSpeed = session_files.get(SessionFileType.maximum_speed).url;
      }
      if (session_files.containsKey(SessionFileType.lap_chart)) {
        output.lapChart = session_files.get(SessionFileType.lap_chart).url;
      }
      if (session_files.containsKey(SessionFileType.grid)) {
        output.grid = session_files.get(SessionFileType.grid).url;
      }
      if (session_files.containsKey(SessionFileType.fast_lap_sequence)) {
        output.fastLapSequence = session_files.get(SessionFileType.fast_lap_sequence).url;
      }
      if (session_files.containsKey(SessionFileType.average_speed)) {
        output.averageSpeed = session_files.get(SessionFileType.average_speed).url;
      }
      if (session_files.containsKey(SessionFileType.world_standing)) {
        output.worldStanding = session_files.get(SessionFileType.world_standing).url;
      }
    }
    return output;
  }
}
