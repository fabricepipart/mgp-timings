package org.teknichrono.mgp.csv.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.csv.model.SeasonCSV;

@ApplicationScoped
public class SeasonCsvConverter extends CsvConverter<Season, SeasonCSV> implements CsvConverterInterface {

  public Class<Season> inputClass() {
    return Season.class;
  }

  public Class<SeasonCSV> outputClass() {
    return SeasonCSV.class;
  }

}
