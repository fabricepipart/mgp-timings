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
      output.analysisByLap = getUrl(session_files, SessionFileType.ANALYSIS_BY_LAP);
      output.fastLapRider = getUrl(session_files, SessionFileType.FAST_LAP_RIDER);
      output.combinedClassification = getUrl(session_files, SessionFileType.COMBINED_CLASSIFICATION);
      output.session = getUrl(session_files, SessionFileType.SESSION);
      output.bestPartialTime = getUrl(session_files, SessionFileType.BEST_PARTIAL_TIME);
      output.combinedPractice = getUrl(session_files, SessionFileType.COMBINED_PRACTICE);
      output.classification = getUrl(session_files, SessionFileType.CLASSIFICATION);
      output.analysis = getUrl(session_files, SessionFileType.ANALYSIS);
      output.maximumSpeed = getUrl(session_files, SessionFileType.MAXIMUM_SPEED);
      output.lapChart = getUrl(session_files, SessionFileType.LAP_CHART);
      output.grid = getUrl(session_files, SessionFileType.GRID);
      output.fastLapSequence = getUrl(session_files, SessionFileType.FAST_LAP_SEQUENCE);
      output.averageSpeed = getUrl(session_files, SessionFileType.AVERAGE_SPEED);
      output.worldStanding = getUrl(session_files, SessionFileType.WORLD_STANDING);
    }
    return output;
  }

  private static String getUrl(Map<SessionFileType, PdfFile> session_files, SessionFileType analysisByLap) {
    if (session_files.containsKey(analysisByLap)) {
      String value = session_files.get(analysisByLap).url;
      if (!"".equalsIgnoreCase(value)) {
        return value;
      }
    }
    return null;
  }
}
