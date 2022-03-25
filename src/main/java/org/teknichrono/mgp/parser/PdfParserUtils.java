package org.teknichrono.mgp.parser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfParserUtils {


  public static String parseNation(String line) {
    Pattern p = Pattern.compile("\\s[A-Z]{3}\\s");
    Matcher m = p.matcher(line);
    while (m.find()) {
      return m.group().trim();
    }
    return null;
  }


  public static Float parseSpeed(String line) {
    Pattern p = Pattern.compile("\\s[0-9]+\\.[0-9]");
    Matcher m = p.matcher(line);
    while (m.find()) {
      return Float.parseFloat(m.group());
    }
    return null;
  }


  public static String parseTime(String line) {
    Pattern p = Pattern.compile("\\s[0-9]+'[0-9]{2}\\.[0-9]{3}");
    Matcher m = p.matcher(line);
    while (m.find()) {
      return m.group().trim();
    }
    return null;
  }


  public static String[] readPdfLines(String url) throws PdfParsingException {
    String[] lines;
    try {
      InputStream in = new URL(url).openStream();
      try (PDDocument doc = PDDocument.load(in)) {
        PDFTextStripper tStripper = new PDFTextStripper();
        tStripper.setSortByPosition(true);
        String pdfFileInText = tStripper.getText(doc);
        // split by whitespace
        lines = pdfFileInText.split("\\r?\\n");
      }
    } catch (IOException e) {
      throw new PdfParsingException("Error when reading the PDF at " + url, e);
    }
    return lines;
  }

  public static Integer parseIntegerIfYouCan(String word) {
    try {
      return Integer.parseInt(word);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  public static Float parseFloatIfYouCan(String word) {
    try {
      return Float.parseFloat(word);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  public static boolean startsWithNumber(String line) {
    Pattern p = Pattern.compile("^[0-9]{1,2}\\s+.*");
    Matcher m = p.matcher(line);
    return m.matches();
  }
}
