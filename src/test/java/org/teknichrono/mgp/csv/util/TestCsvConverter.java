package org.teknichrono.mgp.csv.util;

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
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TestCsvConverter {

  public List<StringCSV> DEFAULT = Arrays.asList(new StringCSV("One"), new StringCSV("Two"), new StringCSV("Three"));


  @Mock
  StatefulBeanToCsv beanToCsv;

  @Test
  public void converts() throws IOException {
    CsvConverter<StringCSV, StringCSV> converter = new CsvConverter<>();
    String csv = converter.convertToCsv(DEFAULT, StringCSV.class);
    Assertions.assertNotNull(csv);
    Assertions.assertNotEquals(0, csv.length());
  }

  @Test
  public void nullReturnEmptyString() throws IOException {
    CsvConverter<StringCSV, StringCSV> converter = new CsvConverter<>();
    String csv = converter.convertToCsv(null, StringCSV.class);
    Assertions.assertNotNull(csv);
    Assertions.assertEquals(0, csv.length());
  }

  @Test
  public void fails() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    CsvConverter<StringCSV, StringCSV> converter = new CSVConverterStub(true);
    Assertions.assertThrows(IOException.class, () -> {
      converter.convertToCsv(DEFAULT, StringCSV.class);
    });
  }


  private class CSVConverterStub extends CsvConverter {


    private CSVConverterStub(boolean fail) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
      if (fail) {
        Mockito.doThrow(new CsvDataTypeMismatchException("Nope")).when(beanToCsv).write(DEFAULT);
      }
    }

    StatefulBeanToCsv<String> getBeanToCsv(Class beanClass) {
      return beanToCsv;
    }
  }

  private class StringCSV implements CSVConvertible<StringCSV> {

    public String s;

    public StringCSV(String s) {
      this.s = s;
    }

    @Override
    public StringCSV toCsv() {
      return this;
    }
  }

}