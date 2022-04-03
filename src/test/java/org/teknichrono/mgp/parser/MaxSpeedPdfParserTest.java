package org.teknichrono.mgp.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.model.out.MaxSpeed;
import org.teknichrono.mgp.model.out.SessionRider;
import org.teknichrono.mgp.model.rider.RiderConstructor;
import org.teknichrono.mgp.model.rider.RiderSeason;
import org.teknichrono.mgp.model.rider.RiderTeam;

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
    ArrayList<SessionRider> riderDetails = getRiderDetails();
    MaxSpeed maxSpeed = parser.parseLine("33 Brad BINDER RSA Red Bull KTM Factory Racing KTM 347.2", riderDetails, 2022);
    Assertions.assertTrue(!maxSpeed.testIfIncomplete());
  }

  private ArrayList<SessionRider> getRiderDetails() {
    ArrayList<SessionRider> riderDetails = new ArrayList<>();
    SessionRider brad = new SessionRider();
    brad.surname = "Binder";
    brad.name = "Brad";
    RiderSeason season = new RiderSeason();
    season.sponsored_team = "Red Bull KTM Factory Racing";
    season.season = 2022;
    season.number = 33;
    RiderConstructor ducati = new RiderConstructor();
    ducati.name = "Ducati";
    season.team = new RiderTeam();
    season.team.constructor = ducati;
    brad.season = season;
    riderDetails.add(brad);
    return riderDetails;
  }


  @Test
  public void cantReadLine() {
    MaxSpeedPdfParser parser = new MaxSpeedPdfParser();
    ArrayList<SessionRider> riderDetails = getRiderDetails();
    Assertions.assertThrows(PdfParsingException.class, () -> {
      parser.parseLine("33 Brad BINDER RSA Red Bull KTM Factory Racing KTM 347.2 somethingelse", new ArrayList<>(), 2022);
    });
  }

}