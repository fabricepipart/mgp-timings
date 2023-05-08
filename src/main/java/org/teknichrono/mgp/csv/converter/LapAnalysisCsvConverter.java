package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.api.model.LapAnalysis;

@ApplicationScoped
public class LapAnalysisCsvConverter extends CsvConverter<LapAnalysis, LapAnalysis> implements CsvConverterInterface {

  public Class<LapAnalysis> inputClass() {
    return LapAnalysis.class;
  }

  public Class<LapAnalysis> outputClass() {
    return LapAnalysis.class;
  }

}
