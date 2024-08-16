package org.teknichrono.mgp.api.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.client.model.result.RiderClassification;

class TestSessionClassificationOutput {

  @Test
  void acceptsEmptyRiderClassification() {
    RiderClassification rc = new RiderClassification();
    Assertions.assertThat(SessionClassificationOutput.from(rc)).isNotNull();
  }
}