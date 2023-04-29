package org.teknichrono.mgp.api.rest.internal;

import org.teknichrono.mgp.business.service.RiderService;
import org.teknichrono.mgp.client.model.rider.RiderDetails;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/internal/rider")
public class InternalRiderEndpoint {

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
