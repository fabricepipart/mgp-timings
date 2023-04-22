package org.teknichrono.mgp.util;

import org.teknichrono.mgp.api.model.result.RiderClassification;
import org.teknichrono.mgp.api.model.result.Season;
import org.teknichrono.mgp.model.csv.RiderClassificationCSV;
import org.teknichrono.mgp.model.csv.SessionClassificationCSV;
import org.teknichrono.mgp.model.out.LapAnalysis;
import org.teknichrono.mgp.model.output.SessionClassificationOutput;

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
