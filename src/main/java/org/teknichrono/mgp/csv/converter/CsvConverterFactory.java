package org.teknichrono.mgp.csv.converter;

import io.quarkus.arc.All;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CsvConverterFactory {

  @Inject
  @All
  public List<CsvConverterInterface> converters;

  public <T> CsvConverterInterface getCsvConverter(Class<T> objectClass) {
    for (CsvConverterInterface converter : converters) {
      if (converter.inputClass().equals(objectClass)) {
        return converter;
      }
    }
    throw new UnsupportedOperationException("No CSV converter available for " + objectClass.getName());
  }
}
