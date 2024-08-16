package org.teknichrono.mgp.api.model;

public class SessionEvent {

  public String time;
  public String message;

  public static SessionEvent from(String time, String message) {
    SessionEvent event = new SessionEvent();
    event.time = time;
    event.message = message;
    return event;
  }
}
