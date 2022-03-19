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

  public String convertToCsv(List<T> results) throws IOException {
    String csvResult;
    StatefulBeanToCsv<T> beanToCsv = getBeanToCsv();
    csvResult = getCsvString(results, beanToCsv);
    return csvResult;
  }

  private String getCsvString(List results, StatefulBeanToCsv<T> seasonToCsv) throws IOException {
    String csvResult;
    try {
      seasonToCsv.write(results);
      csvResult = this.writer.toString();
      this.writer.close();
    } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
      LOGGER.error("Unable to generate CSV", e);
      throw new IOException(e);
    }
    return csvResult;
  }

  StatefulBeanToCsv<T> getBeanToCsv() {
    return new StatefulBeanToCsvBuilder<T>(this.writer).build();
  }

}
