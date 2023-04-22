package org.teknichrono.mgp.api.model;

import org.teknichrono.mgp.client.model.result.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryOutput {

  public static final String RIDERS = "riders";
  private static final String RIDERS_DESCRIPTION = "";

  public String name;
  public List<Choice> additionalOptions;
  public List<Choice> sessions = new ArrayList<>();

  public static CategoryOutput from(Category c) {
    CategoryOutput category = new CategoryOutput();
    category.name = c.name;

    category.additionalOptions = new ArrayList<>();
    category.additionalOptions.add(Choice.from(RIDERS, RIDERS_DESCRIPTION));

    return category;
  }
}
