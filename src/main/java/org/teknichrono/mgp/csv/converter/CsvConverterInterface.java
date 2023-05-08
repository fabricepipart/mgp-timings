package org.teknichrono.mgp.csv.converter;

import java.io.IOException;
import java.util.List;

public interface CsvConverterInterface {

  Class inputClass();

  Class outputClass();

  default String convertToCsv(List convertMe) throws IOException {
    CsvConverter converter = new CsvConverter();
    return converter.convertToCsv(convertMe, outputClass());
  }

}
