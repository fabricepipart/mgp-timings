package org.teknichrono.mgp.csv.converter;

import java.io.IOException;
import java.util.List;

public interface CsvConverterInterface {

  <T> Class<T> inputClass();

  <Y> Class<Y> outputClass();

  default <T extends CSVConvertible<Y>, Y> String convertToCsv(List<T> convertMe) throws IOException {
    CsvConverter<T, Y> converter = new CsvConverter<>();
    return converter.convertToCsv(convertMe, outputClass());
  }

}
