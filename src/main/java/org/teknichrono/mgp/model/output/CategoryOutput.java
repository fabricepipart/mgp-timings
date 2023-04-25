package org.teknichrono.mgp.model.output;

import org.teknichrono.mgp.api.model.result.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryOutput {
  
  public String name;
  public List<Choice> sessions = new ArrayList<>();

  public static CategoryOutput from(Category c) {
    CategoryOutput category = new CategoryOutput();
    category.name = c.name;
    return category;
  }
}
