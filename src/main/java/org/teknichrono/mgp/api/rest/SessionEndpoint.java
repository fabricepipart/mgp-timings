package org.teknichrono.mgp.api.rest;

import org.teknichrono.mgp.business.service.SessionService;
import org.teknichrono.mgp.client.model.result.Session;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import java.util.Optional;

@Path("")
public class SessionEndpoint {

  @Inject
  SessionService sessionService;

  protected Session getSession(int year, String eventShortName, String categoryName, String shortSessionName) {
    Optional<Session> sessionOptional = sessionService.getSessionByName(year, eventShortName, categoryName, shortSessionName);
    if (sessionOptional.isEmpty()) {
      throw new NotFoundException(String.format("Could not find session %s of category %s of %s in %d", shortSessionName, categoryName, eventShortName, year));
    }
    return sessionOptional.get();
  }

}
