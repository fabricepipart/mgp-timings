package org.teknichrono.mgp.api.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.client.model.result.RiderClassification;

class TestSessionClassificationOutput {

  @BeforeEach
  void setUp() {
  }

  @Test
  void acceptsEmptyRiderClassification() {
    RiderClassification rc = new RiderClassification();
    Assertions.assertThat(SessionClassificationOutput.from(rc)).isNotNull();
  }
}