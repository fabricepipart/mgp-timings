package org.teknichrono.mgp.model.out;

import org.teknichrono.mgp.model.result.Classification;

import java.util.List;

public interface ClassificationDetails {

  void fill(Classification c, List<SessionRider> ridersDetails);


}
