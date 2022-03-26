package org.teknichrono.mgp.model.out;

import org.teknichrono.mgp.model.result.Classification;
import org.teknichrono.mgp.model.rider.RiderDetails;

import java.util.List;

public interface ClassificationDetails {

  void fill(Classification c, List<RiderDetails> ridersDetails, int year);


}
