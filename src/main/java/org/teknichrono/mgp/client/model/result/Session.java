package org.teknichrono.mgp.client.model.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {

  public static final String RACE_TYPE = "RAC";

  public String id;
  public boolean test;
  public String type;
  public Integer number;
  public SessionCondition condition;
  public String date;
  public String status;
  public Map<SessionFileType, PdfFile> session_files;
  public String circuit;


  public String getSessionName() {
    if (number != null) {
      return type + number;
    }
    return type;
  }
}
