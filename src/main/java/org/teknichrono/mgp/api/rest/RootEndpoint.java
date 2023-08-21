package org.teknichrono.mgp.api.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.teknichrono.mgp.api.model.RootOutput;
import org.teknichrono.mgp.business.service.SeasonService;

import java.util.stream.Collectors;

@Path("")
public class RootEndpoint {

  @Inject
  SeasonService seasonService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public RootOutput getYears() {
    RootOutput toReturn = new RootOutput();
    toReturn.years.addAll(seasonService.getSeasons().stream().map(it -> it.year).collect(Collectors.toList()));
    return toReturn;
  }

}

