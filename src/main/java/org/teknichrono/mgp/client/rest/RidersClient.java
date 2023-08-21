package org.teknichrono.mgp.client.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.teknichrono.mgp.client.model.rider.RiderDetails;

@RegisterRestClient(configKey = "riders-api")
public interface RidersClient {

  @GET
  @Path("/{id}")
  RiderDetails getRider(@PathParam("id") String id);

}
