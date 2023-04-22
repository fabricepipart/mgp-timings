package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRidersOutput {

  public String category;
  public List<SessionRider> riders = new ArrayList<>();

  public static CategoryRidersOutput from(Category cat, List<SessionRider> sessionRiders) {
    CategoryRidersOutput toReturn = new CategoryRidersOutput();

    toReturn.category = cat.name;
    toReturn.riders = sessionRiders;

    return toReturn;
  }
}
