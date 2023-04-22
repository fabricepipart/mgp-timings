package org.teknichrono.mgp.rest;

import javax.transaction.Transactional;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
@Transactional
public class RestApplication extends Application {


  public RestApplication() {
    super();
  }

}