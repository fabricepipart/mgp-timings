package org.teknichrono.mgp.rest.internal;

import org.teknichrono.mgp.business.RiderService;
import org.teknichrono.mgp.api.model.rider.RiderDetails;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/internal/rider")
public class RiderEndpoint {

  @Inject
  RiderService riderService;


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{legacyId}")
  public RiderDetails getRider(@PathParam("legacyId") Integer legacyId) {
    return riderService.getRider(legacyId);
  }
}
