package org.teknichrono.mgp.util;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class TestCsvConverter {


  @Mock
  StatefulBeanToCsv beanToCsv;

  @Test
  public void converts() throws IOException {
    CsvConverter<String> converter = new CsvConverter<String>();
    String csv = converter.convertToCsv(Arrays.asList("One", "Two", "Three"));
    Assertions.assertNotNull(csv);
    Assertions.assertNotEquals(0, csv.length());
  }

  @Test
  public void nullReturnEmptyString() throws IOException {
    CsvConverter<String> converter = new CsvConverter<String>();
    String csv = converter.convertToCsv(null);
    Assertions.assertNotNull(csv);
    Assertions.assertEquals(0, csv.length());
  }

  @Test
  public void fails() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    CsvConverter<String> converter = new CSVConverterStub(true);
    Assertions.assertThrows(IOException.class, () -> {
      converter.convertToCsv(Arrays.asList("One", "Two", "Three"));
    });
  }


  private class CSVConverterStub extends CsvConverter {

    private CSVConverterStub(boolean fail) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
      if (fail) {
        Mockito.doThrow(new CsvDataTypeMismatchException("Nope")).when(beanToCsv).write(Arrays.asList("One", "Two", "Three"));
      }
    }

    StatefulBeanToCsv<String> getBeanToCsv() {
      return beanToCsv;
    }
  }

}