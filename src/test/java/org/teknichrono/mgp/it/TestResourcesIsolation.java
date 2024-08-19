package org.teknichrono.mgp.it;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class TestResourcesIsolation {

  private static final Logger LOGGER = Logger.getLogger(TestResourcesIsolation.class);

  @Test
  void makeSureAllResourcesUseWiremock() throws Exception {
    ensureIsolation("__files");
  }

  private void ensureIsolation(String resourcesPath) throws Exception {
    LOGGER.info("Testing " + resourcesPath);
    String path = Thread.currentThread().getContextClassLoader().getResource(resourcesPath).getPath();
    File[] files = new File(path).listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        ensureIsolation(resourcesPath + File.separator + file.getName());
      } else {
        if (file.getPath().endsWith(".json")) {
          try (Stream<String> s = Files.lines(Paths.get(file.toURI()))) {
            if (s.anyMatch(it -> it.contains("resources.motogp.com"))) {
              LOGGER.error("The file " + file.getPath() + " contains a URL that is not isolated for tests" +
                  " You should replace 'https://resources.motogp.com' with 'http://localhost:8089'");
              fail();
            }
          }
        }
      }
    }
  }

}
