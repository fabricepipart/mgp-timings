package org.teknichrono.mgp.api.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.teknichrono.mgp.api.model.rider.RiderDetails;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RegisterRestClient(configKey = "riders-api")
public interface RidersClient {

  @GET
  @Path("/riders/{legacyId}")
  RiderDetails getRider(@PathParam("legacyId") Integer legacyId);

}
