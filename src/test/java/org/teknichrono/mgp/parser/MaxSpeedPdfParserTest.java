package org.teknichrono.mgp.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.model.out.MaxSpeed;
import org.teknichrono.mgp.model.rider.Constructor;
import org.teknichrono.mgp.model.rider.RiderDetails;
import org.teknichrono.mgp.model.rider.RiderSeason;
import org.teknichrono.mgp.model.rider.Team;

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
    ArrayList<RiderDetails> riderDetails = getRiderDetails();
    MaxSpeed maxSpeed = parser.parseLine("33 Brad BINDER RSA Red Bull KTM Factory Racing KTM 347.2", riderDetails, 2022);
    Assertions.assertTrue(!maxSpeed.testIfIncomplete());
  }

  private ArrayList<RiderDetails> getRiderDetails() {
    ArrayList<RiderDetails> riderDetails = new ArrayList<>();
    RiderDetails brad = new RiderDetails();
    brad.surname = "Binder";
    brad.name = "Brad";
    RiderSeason season = new RiderSeason();
    season.sponsored_team = "Red Bull KTM Factory Racing";
    season.season = 2022;
    season.number = 33;
    Constructor ducati = new Constructor();
    ducati.name = "Ducati";
    season.team = new Team();
    season.team.constructor = ducati;
    brad.career = new ArrayList<>();
    brad.career.add(season);
    riderDetails.add(brad);
    return riderDetails;
  }


  @Test
  public void cantReadLine() {
    MaxSpeedPdfParser parser = new MaxSpeedPdfParser();
    ArrayList<RiderDetails> riderDetails = getRiderDetails();
    Assertions.assertThrows(PdfParsingException.class, () -> {
      parser.parseLine("33 Brad BINDER RSA Red Bull KTM Factory Racing KTM 347.2 somethingelse", new ArrayList<>(), 2022);
    });
  }

}