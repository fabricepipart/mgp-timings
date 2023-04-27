package org.teknichrono.mgp.business.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.MaxSpeed;
import org.teknichrono.mgp.client.model.result.Constructor;
import org.teknichrono.mgp.client.model.result.Rider;
import org.teknichrono.mgp.client.model.result.RiderClassification;
import org.teknichrono.mgp.client.model.result.Team;

import java.util.ArrayList;

class MaxSpeedPdfParserTest {


  @Test
  public void cantReadPdf() {
    MaxSpeedPdfParser parser = new MaxSpeedPdfParser();
    Assertions.assertThrows(PdfParsingException.class, () -> {
      parser.parse("file://nowayitexists", new ArrayList<>(), 2022);
    });
  }


  @Test
  public void canReadLine() throws PdfParsingException {
    MaxSpeedPdfParser parser = new MaxSpeedPdfParser();
    ArrayList<RiderClassification> riderDetails = getRiderDetails();
    MaxSpeed maxSpeed = parser.parseLine("33 Brad BINDER RSA Red Bull KTM Factory Racing KTM 347.2", riderDetails, 2022);
    Assertions.assertTrue(!maxSpeed.testIfIncomplete());
  }

  private ArrayList<RiderClassification> getRiderDetails() {
    ArrayList<RiderClassification> riderDetails = new ArrayList<>();
    RiderClassification brad = new RiderClassification();
    brad.rider = new Rider();
    brad.rider.full_name = "Brad Binder";
    brad.team = new Team();
    brad.team.name = "Red Bull KTM Factory Racing";
    brad.rider.number = 33;
    brad.constructor = new Constructor();
    brad.constructor.name = "Ducati";
    riderDetails.add(brad);
    return riderDetails;
  }


  @Test
  public void cantReadLine() {
    MaxSpeedPdfParser parser = new MaxSpeedPdfParser();
    ArrayList<RiderClassification> riderDetails = getRiderDetails();
    Assertions.assertThrows(PdfParsingException.class, () -> {
      parser.parseLine("33 Brad BINDER RSA Red Bull KTM Factory Racing KTM 347.2 somethingelse", new ArrayList<>(), 2022);
    });
  }

}