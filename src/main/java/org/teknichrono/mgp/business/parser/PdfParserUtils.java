package org.teknichrono.mgp.business.parser;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


@java.lang.SuppressWarnings("java:S5852")
public class PdfParserUtils {


  public static final String FRONT_TYRE_GROUP = "front";
  public static final String REAR_TYRE_GROUP = "rear";
  public static final String NEW_TYRE = "New Tyre";
  public static final String LINE_ENDING = "\\r?\\n";

  public static String parseNation(String line) {
    Pattern p = Pattern.compile("\\s[A-Z]{3}(\\s|$)");
    Matcher m = p.matcher(line);
    if (m.find()) {
      return m.group().trim();
    }
    return null;
  }


  public static Float parseSpeed(String line) {
    Pattern p = Pattern.compile("\\s[0-9]{3}\\.[0-9]");
    Matcher m = p.matcher(line);
    if (m.find()) {
      return Float.parseFloat(m.group());
    }
    return null;
  }


  public static String parseTime(String line) {
    List<String> times = parseTimes(line);
    if (!times.isEmpty()) {
      return times.get(0);
    }
    return null;
  }

  public static List<String> parseTimes(String line) {
    List<String> toReturn = new ArrayList<>();
    Pattern p = Pattern.compile("\\s\\d*:?\\d*'?\\d{2}\\.\\d{3}");
    Matcher m = p.matcher(line);
    while (m.find()) {
      toReturn.add(m.group().trim());
    }
    return toReturn;
  }

  public static String parseHour(String line) {
    Pattern p = Pattern.compile("\\d+:\\d+'?\\d{2}");
    Matcher m = p.matcher(line);
    if (m.find()) {
      return m.group().trim();
    }
    return null;
  }

  public static List<String> parseIntegers(String line) {
    String lineWithoutLapsToFirst = line.replaceAll("\\d+ laps", "");
    return Stream.of(lineWithoutLapsToFirst.split(" ")).filter(s -> s.matches("\\d+")).toList();
  }

  public static String parseFrontTyre(String line) {
    return parseTyre(line, FRONT_TYRE_GROUP);
  }

  public static String parseRearTyre(String line) {
    return parseTyre(line, REAR_TYRE_GROUP);
  }

  private static String parseTyre(String line, String tyre) {
    Pattern p = Pattern.compile("Run .* Front Tyre (?<" + FRONT_TYRE_GROUP + ">.*) Rear Tyre (?<" + REAR_TYRE_GROUP + ">.*)");
    Matcher m = p.matcher(line);
    if (m.find()) {
      return m.group(tyre).trim();
    }
    return null;
  }

  public static int parseFrontTyreAge(String line) {
    if (Pattern.compile("^\\s*\\**\\s*" + NEW_TYRE).matcher(line).find()) {
      return 0;
    }
    Pattern p = Pattern.compile("^(?<age>[0-9]+) Laps at start");
    Matcher m = p.matcher(line);
    if (m.find()) {
      return Integer.parseInt(m.group("age").trim());
    }
    return -1;
  }

  public static int parseRearTyreAge(String line) {
    if (line.endsWith(NEW_TYRE)) {
      return 0;
    }
    Pattern p = Pattern.compile("(?<age>[0-9]+) Laps at start$");
    Matcher m = p.matcher(line);
    if (m.find()) {
      return Integer.parseInt(m.group("age").trim());
    }
    return -1;
  }


  public static List<String> readPdfLinesTwoColumns(String url, int x, int x2, int y, int width, int height) throws PdfParsingException {
    List<String> lines = new ArrayList<>();
    try {
      InputStream in = new URL(url).openStream();
      try (PDDocument doc = Loader.loadPDF(in.readAllBytes())) {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        Rectangle left = new Rectangle(x, y, width, height);
        Rectangle right = new Rectangle(x2, y, width, height);
        stripper.addRegion("left", left);
        stripper.addRegion("right", right);
        for (Iterator<PDPage> i = doc.getPages().iterator(); i.hasNext(); ) {
          PDPage page = i.next();
          stripper.extractRegions(page);
          String pdfFileInText = stripper.getTextForRegion("left");
          // split by whitespace
          lines.addAll(Arrays.asList(pdfFileInText.split(LINE_ENDING)));
          pdfFileInText = stripper.getTextForRegion("right");
          // split by whitespace
          lines.addAll(Arrays.asList(pdfFileInText.split(LINE_ENDING)));
        }

      }
    } catch (IOException e) {
      throw new PdfParsingException("Error when reading the PDF at " + url, e);
    }
    return lines;
  }


  public static String[] readPdfLines(String url) throws PdfParsingException {
    String[] lines;
    try {
      InputStream in = new URL(url).openStream();
      try (PDDocument doc = Loader.loadPDF(in.readAllBytes())) {
        PDFTextStripper tStripper = new PDFTextStripper();
        tStripper.setSortByPosition(true);
        String pdfFileInText = tStripper.getText(doc);
        // split by whitespace
        lines = pdfFileInText.split(LINE_ENDING);
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

  public static Float parseDurationAsFloat(String s) {
    float toReturn = 0f;
    String[] minutesAndSeconds = s.split("\'");
    if (minutesAndSeconds != null) {
      if (minutesAndSeconds.length == 2) {
        toReturn += 60f * Float.parseFloat(minutesAndSeconds[0]);
      }
      toReturn += Float.parseFloat(minutesAndSeconds[minutesAndSeconds.length - 1]);
    }
    return toReturn;
  }

  public static Integer parseLaps(String line) {
    Pattern p = Pattern.compile("(?<laps>\\d+) lap(s)?");
    Matcher m = p.matcher(line);
    if (m.find()) {
      return Integer.parseInt(m.group("laps").trim());
    }
    return null;
  }
}
