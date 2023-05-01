package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Record;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.client.model.result.SessionResults;

import java.util.ArrayList;
import java.util.List;

public class SessionResultOutput extends SessionOutput {

  public List<RecordOutput> records = new ArrayList<>();
  public List<SessionClassificationOutput> classifications = new ArrayList<>();

  public SessionResultOutput() {
    super(true);
  }

  public static SessionResultOutput from(Session s, SessionResults c, List<SessionClassificationOutput> results) {
    SessionResultOutput session = new SessionResultOutput();
    session.fillFromSession(s);
    session.fillFromClassification(c);
    session.classifications = results;

    return session;
  }

  private void fillFromClassification(SessionResults c) {
    if (c.records != null && !c.records.isEmpty()) {
      for (Record r : c.records) {
        records.add(RecordOutput.from(r));
      }
    }
  }

}
