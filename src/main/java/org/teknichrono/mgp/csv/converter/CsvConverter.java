package org.teknichrono.mgp.csv.converter;

import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

public class CsvConverter<T extends CSVConvertible<Y>, Y> {

  private static final Logger LOGGER = Logger.getLogger(CsvConverter.class);

  private StringWriter writer = new StringWriter();

  public String convertToCsv(List<T> results, Class<Y> outputClass) throws IOException {
    String csvResult;
    StatefulBeanToCsv<Y> beanToCsv = getBeanToCsv(outputClass);
    List<Y> outputList = null;
    if (results != null) {
      outputList = results.stream().map(r -> r.toCsv()).collect(Collectors.toList());
    }
    csvResult = getCsvString(outputList, beanToCsv);
    return csvResult;
  }

  private String getCsvString(List<Y> outputList, StatefulBeanToCsv<Y> beanToCsv) throws IOException {
    String csvResult;
    try {
      beanToCsv.write(outputList);
      csvResult = this.writer.toString();
      this.writer.close();
    } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
      LOGGER.error("Unable to generate CSV", e);
      throw new IOException(e);
    }
    return csvResult;
  }

  StatefulBeanToCsv<Y> getBeanToCsv(Class<Y> beanClass) {
    MappingStrategy<Y> mappingStrategy = new CustomMappingStrategy<>();
    mappingStrategy.setType(beanClass);
    return new StatefulBeanToCsvBuilder<Y>(this.writer).withMappingStrategy(mappingStrategy).build();
  }

}
