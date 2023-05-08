package org.teknichrono.mgp.csv.rest;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.teknichrono.mgp.csv.converter.CSVConvertible;
import org.teknichrono.mgp.csv.converter.CsvConverterFactory;
import org.teknichrono.mgp.csv.converter.CsvConverterInterface;

import java.io.IOException;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class TestCSVEndpoint {

  @Mock
  CsvConverterInterface csvConverter;

  @Mock
  CsvConverterFactory csvConverterFactory;


  @Test
  void fails() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    Mockito.when(csvConverterFactory.getCsvConverter(MyConvertibleObject.class)).thenReturn(csvConverter);
    Mockito.doThrow(new IOException("nope")).when(csvConverter).convertToCsv(Mockito.any());
    FakeCSVEndpoint endpoint = new FakeCSVEndpoint(csvConverterFactory);
    MyConvertibleObject a = new MyConvertibleObject();
    MyConvertibleObject b = new MyConvertibleObject();
    Response response = endpoint.csvOutput(Arrays.asList(a, b), MyConvertibleObject.class, "file.csv");
    Assertions.assertThat(response.getStatus()).isEqualTo(500);
  }

  private class FakeCSVEndpoint extends CSVEndpoint {

    public FakeCSVEndpoint(CsvConverterFactory factory) {
      super.csvConverterFactory = factory;
    }
  }

  private class MyConvertibleObject implements CSVConvertible<String> {

    @Override
    public String toCsv() {
      return "";
    }
  }

}