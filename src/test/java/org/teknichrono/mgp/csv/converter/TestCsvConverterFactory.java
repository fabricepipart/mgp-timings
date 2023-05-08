package org.teknichrono.mgp.csv.converter;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@QuarkusTest
class TestCsvConverterFactory {

  @InjectSpy
  CsvConverterFactory csvConverterFactory;

  @Test
  void thereIsAConverterForEachCSVConvertible() {
    Set<Class> csvClasses = findAllClassesUsingClassLoader("org.teknichrono.mgp.csv.model");
    Assertions.assertThat(csvClasses).isNotNull().isNotEmpty();
    for (Class csvClass : csvClasses) {
      Optional<CsvConverterInterface> first = csvConverterFactory.converters.stream().filter(c -> c.outputClass().equals(csvClass)).findFirst();
      Assertions.assertThat(first.isPresent()).isTrue();
      Assertions.assertThat(first.get()).isNotNull();
    }
  }

  public Set<Class> findAllClassesUsingClassLoader(String packageName) {
    InputStream stream = ClassLoader.getSystemClassLoader()
        .getResourceAsStream(packageName.replaceAll("[.]", "/"));
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    return reader.lines()
        .filter(line -> line.endsWith(".class"))
        .map(line -> getClass(line, packageName))
        .collect(Collectors.toSet());
  }

  private Class getClass(String className, String packageName) {
    try {
      return Class.forName(packageName + "."
          + className.substring(0, className.lastIndexOf('.')));
    } catch (ClassNotFoundException e) {
      // handle the exception
    }
    return null;
  }
/*
  @Test
  void cachesSeasonCsvConverter() {
    CsvConverter<Season, SeasonCSV> seasonCsvConverter = factory.getSeasonCsvConverter();
    int hash = seasonCsvConverter.hashCode();
    Assertions.assertThat(hash).isEqualTo(factory.getSeasonCsvConverter().hashCode());
  }

  @Test
  void cachesRiderCsvConverter() {
    CsvConverter<RiderClassification, RiderClassificationCSV> converter = factory.getRiderCsvConverter();
    int hash = converter.hashCode();
    Assertions.assertThat(hash).isEqualTo(factory.getRiderCsvConverter().hashCode());
  }

  @Test
  void cachesClassificationCsvConverter() {
    CsvConverter<SessionClassificationOutput, SessionClassificationCSV> converter = factory.getClassificationCsvConverter();
    int hash = converter.hashCode();
    Assertions.assertThat(hash).isEqualTo(factory.getClassificationCsvConverter().hashCode());
  }

  @Test
  void cachesLapAnalysisCsvConverter() {
    CsvConverter<LapAnalysis, LapAnalysis> converter = factory.getLapAnalysisCsvConverter();
    int hash = converter.hashCode();
    Assertions.assertThat(hash).isEqualTo(factory.getLapAnalysisCsvConverter().hashCode());
  }*/
}