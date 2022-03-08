package org.teknichrono.util;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.jboss.logging.Logger;
import org.teknichrono.model.client.Classification;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class ClassificationCsvConverter {


  private static final Logger LOGGER = Logger.getLogger(SeasonCsvConverter.class);

  private StringWriter writer = new StringWriter();

  public String convertToCsv(List<Classification> results) throws IOException {
    String csvResult;
    StatefulBeanToCsv<Classification> seasonToCsv = getClassificationToCsv();
    try {
      seasonToCsv.write(results);
      csvResult = this.writer.toString();
      this.writer.close();
    } catch (CsvRequiredFieldEmptyException e) {
      LOGGER.error("Unable to generate Classification CSV (required field error)", e);
      throw new IOException(e);
    } catch (CsvDataTypeMismatchException e) {
      LOGGER.error("Unable to generate Classification CSV (data type error)", e);
      throw new IOException(e);
    }
    return csvResult;
  }

  private StatefulBeanToCsv<Classification> getClassificationToCsv() {
    return new StatefulBeanToCsvBuilder<Classification>(this.writer).build();
  }
}
