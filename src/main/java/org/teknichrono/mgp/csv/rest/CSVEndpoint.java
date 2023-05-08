package org.teknichrono.mgp.csv.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.teknichrono.mgp.csv.converter.CSVConvertible;
import org.teknichrono.mgp.csv.converter.CsvConverterFactory;

import java.io.IOException;
import java.util.List;

public class CSVEndpoint {

  public static final String CSV_MEDIA_TYPE = "text/csv";
  public static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
  public static final String ATTACHMENT_FILENAME = "attachment;filename=";

  @Inject
  CsvConverterFactory csvConverterFactory;

  protected <T extends CSVConvertible> Response csvOutput(List<T> outputs, Class<T> outputClass, String filename) {
    try {
      String csvResults = csvConverterFactory.getCsvConverter(outputClass).convertToCsv(outputs);
      return Response.ok().entity(csvResults).header(CONTENT_DISPOSITION_HEADER, ATTACHMENT_FILENAME + filename).build();
    } catch (IOException e) {
      return Response.serverError().build();
    }
  }
}
