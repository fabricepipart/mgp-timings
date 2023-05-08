package org.teknichrono.mgp.csv.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.teknichrono.mgp.business.service.SeasonService;
import org.teknichrono.mgp.client.model.result.Season;
import org.teknichrono.mgp.csv.converter.CsvConverterFactory;

@Path("/csv")
public class SeasonCSVEndpoint extends CSVEndpoint {

  @Inject
  SeasonService seasonService;

  @Inject
  CsvConverterFactory csvConverterFactory;

  @GET
  @Produces(CSV_MEDIA_TYPE)
  @Transactional
  public Response getYears() {
    return csvOutput(seasonService.getSeasons(), Season.class, "seasons.csv");
  }

}

