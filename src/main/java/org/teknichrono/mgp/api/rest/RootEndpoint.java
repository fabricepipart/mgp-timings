package org.teknichrono.mgp.api.rest;

import org.teknichrono.mgp.business.service.SeasonService;
import org.teknichrono.mgp.api.model.RootOutput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    toReturn.years.addAll(seasonService.getTestSeasons().stream().map(it -> it.year).collect(Collectors.toList()));
    return toReturn;
  }

}

