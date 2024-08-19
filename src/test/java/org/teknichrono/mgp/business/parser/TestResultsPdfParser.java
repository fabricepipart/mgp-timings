package org.teknichrono.mgp.business.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.api.model.RiderOutput;
import org.teknichrono.mgp.api.model.SessionClassificationOutput;

class TestResultsPdfParser {

  private SessionClassificationOutput details;

  @BeforeEach
  public void prepare() {
    details = new SessionClassificationOutput();
    details.rider = new RiderOutput();
  }

  @Test
  void parseEmptyInteger() {
    String line = "";
    ResultsPdfParser.parseIntegersOfLine(details, line);
    Assertions.assertThat(details.position).isNull();
    Assertions.assertThat(details.points).isNull();
    Assertions.assertThat(details.rider.number).isNull();
  }

  @Test
  void parseOneInteger() {
    String line = "99 Rider Tester";
    ResultsPdfParser.parseIntegersOfLine(details, line);
    Assertions.assertThat(details.position).isNull();
    Assertions.assertThat(details.points).isNull();
    Assertions.assertThat(details.rider.number).isEqualTo(99);
  }

  @Test
  void parseTwoIntegers() {
    String line = "1 22 Rider Tester";
    ResultsPdfParser.parseIntegersOfLine(details, line);
    Assertions.assertThat(details.position).isEqualTo(1);
    Assertions.assertThat(details.points).isNull();
    Assertions.assertThat(details.rider.number).isEqualTo(22);
  }

  @Test
  void parseThreeIntegers() {
    String line = "99 123 5 Rider TESTER";
    ResultsPdfParser.parseIntegersOfLine(details, line);
    Assertions.assertThat(details.position).isEqualTo(99);
    Assertions.assertThat(details.points).isEqualTo(123);
    Assertions.assertThat(details.rider.number).isEqualTo(5);
  }

  @Test
  void parseFourIntegers() {
    String line = "1 20 300 99 Your Tester and more";
    ResultsPdfParser.parseIntegersOfLine(details, line);
    Assertions.assertThat(details.position).isNull();
    Assertions.assertThat(details.points).isNull();
    Assertions.assertThat(details.rider.number).isNull();
  }

  @Test
  void noMatchIfRiderOrNameIsNull() {
    SessionClassificationOutput sessionResult = new SessionClassificationOutput();
    Assertions.assertThat(ResultsPdfParser.lineAndResultMatch("1 2 3 Super Pilot FRA 1'23.456", sessionResult)).isFalse();
    sessionResult.rider = new RiderOutput();
    Assertions.assertThat(ResultsPdfParser.lineAndResultMatch("1 2 3 Super Pilot FRA 1'23.456", sessionResult)).isFalse();
  }

  @Test
  void matchLineAndRider() {
    SessionClassificationOutput sessionResult = new SessionClassificationOutput();
    sessionResult.rider = new RiderOutput();
    sessionResult.rider.number = 3;
    sessionResult.rider.full_name = "Super Pilot";
    Assertions.assertThat(ResultsPdfParser.lineAndResultMatch("1 2 3 Super Pilot FRA 1'23.456", sessionResult)).isTrue();
  }


}