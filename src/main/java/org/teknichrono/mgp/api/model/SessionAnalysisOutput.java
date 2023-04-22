package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionAnalysisOutput extends SessionOutput {


  public List<LapAnalysis> analysis = new ArrayList<>();

  public static SessionAnalysisOutput from(Session session, List<LapAnalysis> lapAnalyses) {
    SessionAnalysisOutput toReturn = new SessionAnalysisOutput();

    toReturn.fillFromSession(session);
    toReturn.analysis = lapAnalyses;

    return toReturn;
  }
}
