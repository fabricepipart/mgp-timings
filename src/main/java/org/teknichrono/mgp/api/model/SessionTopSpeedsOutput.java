package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionTopSpeedsOutput extends SessionOutput {

  public List<MaxSpeed> topSpeeds = new ArrayList<>();

  public static SessionTopSpeedsOutput from(Session session, List<MaxSpeed> maxSpeeds) {
    SessionTopSpeedsOutput toReturn = new SessionTopSpeedsOutput();

    toReturn.fillFromSession(session);
    toReturn.topSpeeds = maxSpeeds;

    return toReturn;
  }
}
