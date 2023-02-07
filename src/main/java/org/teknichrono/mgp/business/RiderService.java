package org.teknichrono.mgp.business;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.teknichrono.mgp.client.RidersClient;
import org.teknichrono.mgp.model.rider.RiderDetails;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RiderService {

  @Inject
  @RestClient
  RidersClient riderClient;


  public RiderDetails getRider(Integer legacyId) {
    return riderClient.getRider(legacyId);
  }

  public List<RiderDetails> getRiders(List<Integer> legacyIds) {
    List<RiderDetails> toReturn = new ArrayList<>();
    if (legacyIds != null) {
      for (Integer id : legacyIds) {
        toReturn.add(riderClient.getRider(id));
      }
    }
    return toReturn;
  }

}
