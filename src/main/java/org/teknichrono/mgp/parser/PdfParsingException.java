package org.teknichrono.mgp.parser;

public class PdfParsingException extends Exception {

  public PdfParsingException(String message) {
    super(message);
  }

  public PdfParsingException(String message, Exception e) {
    super(message, e);
  }
}
