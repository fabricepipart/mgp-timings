package org.teknichrono.mgp.model.result;

import java.util.Map;

public class Session {

  public static final String FILENAME_MAX_SPEED = "maximum_speed";

  public static final String RACE_TYPE = "RAC";

  public String id;
  public String type;
  public Integer number;
  public SessionCondition condition;
  public String date;
  public String status;
  public Map<String, PdfFile> session_files;
  public String circuit;


  public String getSessionName(Session s) {
    if (s.number != null) {
      return s.type + s.number;
    }
    return s.type;
  }
}

