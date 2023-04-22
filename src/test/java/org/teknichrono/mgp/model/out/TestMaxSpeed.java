package org.teknichrono.mgp.model.out;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.MaxSpeed;

class TestMaxSpeed {

  @Test
  public void isCompleteUntilAllFilled() {
    MaxSpeed speed = new MaxSpeed();
    Assertions.assertTrue(speed.testIfIncomplete());
    speed.number = 5;
    Assertions.assertTrue(speed.testIfIncomplete());
    speed.rider = "Miguel Oliveira";
    Assertions.assertTrue(speed.testIfIncomplete());
    speed.nation = "FRA";
    Assertions.assertTrue(speed.testIfIncomplete());
    speed.team = "Yamaha Racing Team";
    Assertions.assertTrue(speed.testIfIncomplete());
    speed.motorcycle = "Ducati";
    Assertions.assertTrue(speed.testIfIncomplete());
    speed.speed = 123.4f;
    Assertions.assertFalse(speed.testIfIncomplete());
  }

}