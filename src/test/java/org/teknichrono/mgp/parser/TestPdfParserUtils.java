package org.teknichrono.mgp.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestPdfParserUtils {


  @Test
  public void returnsNullIfCantFindNation() {
    Assertions.assertNull(PdfParserUtils.parseNation("IT FRANCE 237"));
  }

  @Test
  public void returnsNullFloatIfNotParseable() {
    Assertions.assertNull(PdfParserUtils.parseFloatIfYouCan("not a float"));
  }

  @Test
  public void returnsNullIntegerIfNotParseable() {
    Assertions.assertNull(PdfParserUtils.parseIntegerIfYouCan("not an integer"));
  }

}