package org.teknichrono.mgp.api.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class TestSessionFilesOutput {

  @Test
  void acceptsNullInputs() {

    SessionFilesOutput sessionFilesOutput = SessionFilesOutput.from(null);
    Assertions.assertThat(sessionFilesOutput).isNotNull();
  }

  @Test
  void acceptsEmptyInputs() {

    SessionFilesOutput sessionFilesOutput = SessionFilesOutput.from(new HashMap<>());
    Assertions.assertThat(sessionFilesOutput).isNotNull();
  }
}