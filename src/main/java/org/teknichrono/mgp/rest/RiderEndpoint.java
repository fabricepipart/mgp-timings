package org.teknichrono.mgp.rest;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.teknichrono.mgp.client.RidersService;
import org.teknichrono.mgp.model.rider.RiderDetails;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rider")
public class RiderEndpoint {

  private static final Logger LOGGER = Logger.getLogger(SessionEndpoint.class);

  @Inject
  @RestClient
  RidersService riderService;


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Path("/{legacyId}")
  public RiderDetails getRider(@PathParam("legacyId") Integer legacyId) {
    return riderService.getRider(legacyId);
  }

}
