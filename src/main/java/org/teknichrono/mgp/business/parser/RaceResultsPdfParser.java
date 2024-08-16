package org.teknichrono.mgp.business.parser;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.api.model.CountryOutput;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.api.model.SessionEvent;
import org.teknichrono.mgp.api.model.SessionResultOutput;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RaceResultsPdfParser extends ResultsPdfParser {

  protected void fillSessionDataIfNecessary(String line, SessionResultOutput sessionResults) {
    String time = PdfParserUtils.parseHour(line);
    if (time != null && line.trim().startsWith(time)) {
      String message = line.replace(time, "").trim();
      sessionResults.events.add(SessionEvent.from(time, message));
    }
  }

  protected void completeResultInfo(String line, SessionClassificationOutput details) {
    String nation = PdfParserUtils.parseNation(line);
    details.rider.country = CountryOutput.from(nation);

    parseIntegersOfLine(details, line.split(nation)[0]);

    details.averageSpeed = Optional.ofNullable(details.averageSpeed).orElse(PdfParserUtils.parseSpeed(line));
    List<String> durations = PdfParserUtils.parseTimes(line);
    if (durations != null && !durations.isEmpty()) {
      details.totalTime = durations.get(0);
      if (durations.size() > 1) {
        Float gapToFirst = PdfParserUtils.parseDurationAsFloat(durations.get(1));
        details.gapToFirst = Optional.ofNullable(details.gapToFirst).orElse(gapToFirst);
      }
      Integer lapsToFirst = PdfParserUtils.parseLaps(line);
      if (lapsToFirst != null) {
        details.lapsToFirst = Optional.ofNullable(details.lapsToFirst).orElse(lapsToFirst);
      }
    }
  }
}
