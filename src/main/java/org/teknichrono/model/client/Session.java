package org.teknichrono.model.client;

import java.util.Map;

public class Session {

  public String id;
  public String type;
  public Integer number;
  public SessionCondition condition;
  public String date;
  public String status;
  public Map<String, PdfFile> session_files;
  public String circuit;
}

