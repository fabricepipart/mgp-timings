package org.teknichrono.mgp.api.rest.internal;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.teknichrono.mgp.business.service.RiderService;
import org.teknichrono.mgp.client.model.rider.RiderDetails;

@Path("/internal/rider")
public class InternalRiderEndpoint {

  @Inject
  RiderService riderService;


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{id}")
  public RiderDetails getRider(@PathParam("id") String id) {
    return riderService.getRider(id);
  }
}
