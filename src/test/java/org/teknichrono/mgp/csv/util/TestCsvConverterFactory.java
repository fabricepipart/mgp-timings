package org.teknichrono.mgp.csv.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;
import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.csv.model.RiderClassificationCSV;
import org.teknichrono.mgp.csv.model.SessionClassificationCSV;

class TestCsvConverterFactory {

  private CsvConverterFactory factory;

  @BeforeEach
  void prepare() {
    factory = new CsvConverterFactory();
  }

  @Test
  void cachesSeasonCsvConverter() {
    CsvConverter<Season, Season> seasonCsvConverter = factory.getSeasonCsvConverter();
    int hash = seasonCsvConverter.hashCode();
    Assertions.assertThat(hash).isEqualTo(factory.getSeasonCsvConverter().hashCode());
  }

  @Test
  void cachesRiderCsvConverter() {
    CsvConverter<RiderClassification, RiderClassificationCSV> converter = factory.getRiderCsvConverter();
    int hash = converter.hashCode();
    Assertions.assertThat(hash).isEqualTo(factory.getRiderCsvConverter().hashCode());
  }

  @Test
  void cachesClassificationCsvConverter() {
    CsvConverter<SessionClassificationOutput, SessionClassificationCSV> converter = factory.getClassificationCsvConverter();
    int hash = converter.hashCode();
    Assertions.assertThat(hash).isEqualTo(factory.getClassificationCsvConverter().hashCode());
  }

  @Test
  void cachesLapAnalysisCsvConverter() {
    CsvConverter<LapAnalysis, LapAnalysis> converter = factory.getLapAnalysisCsvConverter();
    int hash = converter.hashCode();
    Assertions.assertThat(hash).isEqualTo(factory.getLapAnalysisCsvConverter().hashCode());
  }
}