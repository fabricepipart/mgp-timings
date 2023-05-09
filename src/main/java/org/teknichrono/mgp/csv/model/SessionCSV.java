package org.teknichrono.mgp.csv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.teknichrono.mgp.client.model.result.Session;

public class SessionCSV {


  @CsvBindByName(column = "NAME")
  @CsvBindByPosition(position = 0)
  public String name;

  @CsvBindByName(column = "TEST")
  @CsvBindByPosition(position = 1)
  public boolean test;

  @CsvBindByName(column = "DATE")
  @CsvBindByPosition(position = 2)
  public String date;

  @CsvBindByName(column = "STATUS")
  @CsvBindByPosition(position = 3)
  public String status;

  @CsvBindByName(column = "CIRCUIT")
  @CsvBindByPosition(position = 4)
  public String circuit;


  public static SessionCSV from(Session session) {
    SessionCSV csv = new SessionCSV();
    csv.name = session.getSessionName();
    csv.test = session.test;
    csv.date = session.date;
    csv.status = session.status;
    csv.circuit = session.circuit;
    return csv;
  }
}
