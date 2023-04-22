package org.teknichrono.mgp.csv.util;

import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.csv.model.RiderClassificationCSV;
import org.teknichrono.mgp.csv.model.SessionClassificationCSV;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CsvConverterFactory {

  private CsvConverter<Season, Season> seasonCsvConverter;

  private CsvConverter<RiderClassification, RiderClassificationCSV> riderCsvConverter;

  private CsvConverter<SessionClassificationOutput, SessionClassificationCSV> classificationCsvConverter;

  private CsvConverter<LapAnalysis, LapAnalysis> lapAnalysisCsvConverter;

  public CsvConverter<Season, Season> getSeasonCsvConverter() {
    if (seasonCsvConverter == null) {
      seasonCsvConverter = new CsvConverter<Season, Season>();
    }
    return seasonCsvConverter;
  }

  public CsvConverter<RiderClassification, RiderClassificationCSV> getRiderCsvConverter() {
    if (riderCsvConverter == null) {
      riderCsvConverter = new CsvConverter<RiderClassification, RiderClassificationCSV>();
    }
    return riderCsvConverter;
  }

  public CsvConverter<SessionClassificationOutput, SessionClassificationCSV> getClassificationCsvConverter() {
    if (classificationCsvConverter == null) {
      classificationCsvConverter = new CsvConverter<SessionClassificationOutput, SessionClassificationCSV>();
    }
    return classificationCsvConverter;
  }

  public CsvConverter<LapAnalysis, LapAnalysis> getLapAnalysisCsvConverter() {
    if (lapAnalysisCsvConverter == null) {
      lapAnalysisCsvConverter = new CsvConverter<LapAnalysis, LapAnalysis>();
    }
    return lapAnalysisCsvConverter;
  }


}
