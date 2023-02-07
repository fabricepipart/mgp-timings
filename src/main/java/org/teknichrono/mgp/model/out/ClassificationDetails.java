package org.teknichrono.mgp.model.out;

import org.teknichrono.mgp.model.result.RiderClassification;
import org.teknichrono.mgp.model.rider.RiderDetails;

import java.util.List;

public interface ClassificationDetails {

  void fill(RiderClassification c, List<RiderDetails> ridersDetails, int year);


}
