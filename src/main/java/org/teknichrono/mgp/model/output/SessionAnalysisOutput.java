package org.teknichrono.mgp.model.output;

import org.teknichrono.mgp.api.model.result.Session;
import org.teknichrono.mgp.model.out.LapAnalysis;

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
