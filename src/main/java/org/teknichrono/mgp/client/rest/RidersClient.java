package org.teknichrono.mgp.client.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.teknichrono.mgp.client.model.rider.RiderDetails;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterRestClient(configKey = "riders-api")
public interface RidersClient {

  @GET
  @Path("/riders/{legacyId}")
  RiderDetails getRider(@PathParam("legacyId") Integer legacyId);

}
