package org.teknichrono.mgp.api.rest.internal;

import org.teknichrono.mgp.business.service.RiderService;
import org.teknichrono.mgp.client.model.rider.RiderDetails;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
