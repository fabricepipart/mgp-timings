package org.teknichrono.mgp.business.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.LapAnalysis;
import org.teknichrono.mgp.client.model.result.RiderClassification;

import java.util.ArrayList;
import java.util.List;

class TestAnalysisPdfParser {

  @BeforeEach
  void setUp() {
  }

  @Test
  void returnsEmptyIfNoUrlPassed() throws PdfParsingException {
    AnalysisPdfParser parser = new AnalysisPdfParser();

    List<RiderClassification> classifications = new ArrayList<>();
    classifications.add(new RiderClassification());
    List<LapAnalysis> parsed = parser.parse(null, classifications);
    Assertions.assertThat(parsed).isNotNull();
    Assertions.assertThat(parsed).isEmpty();
  }
}