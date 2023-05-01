package org.teknichrono.mgp.business.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestPdfParserUtils {


  @Test
  void returnsNullIfCantFindNation() {
    Assertions.assertNull(PdfParserUtils.parseNation("IT FRANCE 237"));
  }

  @Test
  void returnsNullFloatIfNotParseable() {
    Assertions.assertNull(PdfParserUtils.parseFloatIfYouCan("not a float"));
  }

  @Test
  void returnsNullIntegerIfNotParseable() {
    Assertions.assertNull(PdfParserUtils.parseIntegerIfYouCan("not an integer"));
  }

  @Test
  void cantReadPdf() {
    MaxSpeedPdfParser parser = new MaxSpeedPdfParser();
    Assertions.assertThrows(PdfParsingException.class, () -> {
      PdfParserUtils.readPdfLinesTwoColumns("file://nowayitexists", 1, 2, 3, 4, 5);
    });
  }


}