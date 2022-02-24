package org.teknichrono.rest;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/season")
public class SeasonEndpoint {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public Response listAll() {
    try {
      return Response.ok().build();
    } catch (IllegalArgumentException e) {
      return Response.status(Status.BAD_REQUEST).entity(e.toString()).build();
    }
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/hello")
  public String hello() {
    return "Hello MotoGP";
  }
}

