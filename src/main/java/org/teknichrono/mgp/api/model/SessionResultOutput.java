package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Record;
import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.client.model.result.SessionResults;

import java.util.ArrayList;
import java.util.List;

public class SessionResultOutput extends SessionOutput {

  public List<RecordOutput> records = new ArrayList<>();
  public List<SessionClassificationOutput> classifications = new ArrayList<>();
  public List<SessionEvent> events = new ArrayList<>();

  public SessionResultOutput() {
    super(true);
  }

  public static SessionResultOutput from(Session s) {
    SessionResultOutput session = new SessionResultOutput();
    session.fillFromSession(s);
    return session;
  }

  public void fillRecords(SessionResults sessionResults) {
    if (sessionResults.records != null) {
      for (Record r : sessionResults.records) {
        records.add(RecordOutput.from(r));
      }
    }
  }

  public void fillWith(List<RiderClassification> riderClassification) {
    if (riderClassification != null) {
      for (RiderClassification c : riderClassification) {
        SessionClassificationOutput details = SessionClassificationOutput.from(c);
        classifications.add(details);
      }
    }
  }
}
