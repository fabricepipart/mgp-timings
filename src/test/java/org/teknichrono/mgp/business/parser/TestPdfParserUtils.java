package org.teknichrono.mgp.business.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TestPdfParserUtils {


  @Test
  void returnsNullIfCantFindNation() {
    Assertions.assertThat(PdfParserUtils.parseNation("IT FRANCE 237")).isNull();
  }

  @Test
  void returnsNullFloatIfNotParseable() {
    Assertions.assertThat(PdfParserUtils.parseFloatIfYouCan("not a float")).isNull();
  }

  @Test
  void returnsNullIntegerIfNotParseable() {
    Assertions.assertThat(PdfParserUtils.parseIntegerIfYouCan("not an integer")).isNull();
  }

  @Test
  void cantReadPdf() {
    org.junit.jupiter.api.Assertions.assertThrows(PdfParsingException.class, () -> {
      PdfParserUtils.readPdfLinesTwoColumns("file://nowayitexists", 1, 2, 3, 4, 5);
    });
  }

  @Test
  void testReadsFirstTime() {
    String line = "1 36'35.345 31.640 15.695 31.856 31.120 283.4";
    String time = PdfParserUtils.parseTime(line);
    Assertions.assertThat(time).isNotNull().startsWith("36").isEqualTo("36'35.345");
  }

  @Test
  void testCantReadFirstTimeIfEmpty() {
    String time = PdfParserUtils.parseTime("");
    Assertions.assertThat(time).isNull();
  }

  @Test
  void testCantReadFirstTimeIfDoesNotStartWithDigit() {
    String time = PdfParserUtils.parseTime("This is not what you need");
    Assertions.assertThat(time).isNull();
  }

  @Test
  void testReadsTimes() {
    String line = "1 36'35.345 31.640 15.695 31.856 31.120 283.4";
    List<String> times = PdfParserUtils.parseTimes(line);
    Assertions.assertThat(times).isNotNull().hasSize(5);
    Assertions.assertThat(times.get(0)).isEqualTo("36'35.345");
  }


}