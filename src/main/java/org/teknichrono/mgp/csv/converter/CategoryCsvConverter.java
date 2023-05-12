package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.client.model.result.Category;
import org.teknichrono.mgp.csv.model.CategoryCSV;

@ApplicationScoped
public class CategoryCsvConverter extends CsvConverter<Category, CategoryCSV> implements CsvConverterInterface {

  public Class<Category> inputClass() {
    return Category.class;
  }

  public Class<CategoryCSV> outputClass() {
    return CategoryCSV.class;
  }

}
