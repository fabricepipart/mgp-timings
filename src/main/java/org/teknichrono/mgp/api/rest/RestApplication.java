package org.teknichrono.mgp.api.rest;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@Transactional
public class RestApplication extends Application {


  public RestApplication() {
    super();
  }

}