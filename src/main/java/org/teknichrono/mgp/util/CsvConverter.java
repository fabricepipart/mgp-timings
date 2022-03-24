package org.teknichrono.mgp.util;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@RequestScoped
public class CsvConverter<T> {

  private static final Logger LOGGER = Logger.getLogger(CsvConverter.class);

  private StringWriter writer = new StringWriter();

  public String convertToCsv(List<T> results, Class<T> beanClass) throws IOException {
    String csvResult;
    StatefulBeanToCsv<T> beanToCsv = getBeanToCsv(beanClass);
    csvResult = getCsvString(results, beanToCsv);
    return csvResult;
  }

  private String getCsvString(List results, StatefulBeanToCsv<T> beanToCsv) throws IOException {
    String csvResult;
    try {
      beanToCsv.write(results);
      csvResult = this.writer.toString();
      this.writer.close();
    } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
      LOGGER.error("Unable to generate CSV", e);
      throw new IOException(e);
    }
    return csvResult;
  }

  StatefulBeanToCsv<T> getBeanToCsv(Class<T> beanClass) {
    CustomMappingStrategy<T> mappingStrategy = new CustomMappingStrategy<T>();
    mappingStrategy.setType(beanClass);
    return new StatefulBeanToCsvBuilder<T>(this.writer).withMappingStrategy(mappingStrategy).build();
  }

}
