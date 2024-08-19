package org.teknichrono.mgp.business.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.teknichrono.mgp.client.model.result.ClassificationFiles;
import org.teknichrono.mgp.client.model.result.Session;
import org.teknichrono.mgp.client.model.result.SessionFileType;
import org.teknichrono.mgp.client.model.result.SessionResults;

import java.util.Map;
import java.util.Optional;

class TestSessionService {

  @Test
  void getPdfUrlIsNullIfObjectsAreEmpty() {
    Session session = new Session();
    SessionResults sessionResults = new SessionResults();
    String pdfUrl = SessionService.getPdfUrl(Optional.of(session), Optional.of(sessionResults));
    Assertions.assertThat(pdfUrl).isNull();
  }

  @Test
  void getPdfUrlIsNullIfObjectsAreNull() {
    String pdfUrl = SessionService.getPdfUrl(Optional.empty(), Optional.empty());
    Assertions.assertThat(pdfUrl).isNull();
  }

  @Test
  void getPdfUrlReturnsUrlOfSessionResultsFilesFirst() {
    Session session = new Session();
    session.session_files = Map.of(SessionFileType.CLASSIFICATION, "3");
    SessionResults sessionResults = new SessionResults();
    sessionResults.file = "2";
    sessionResults.files = new ClassificationFiles();
    sessionResults.files.classification = "1";
    String pdfUrl = SessionService.getPdfUrl(Optional.of(session), Optional.of(sessionResults));
    Assertions.assertThat(pdfUrl).isNotNull().isEqualTo("1");
  }

  @Test
  void getPdfUrlReturnsUrlOfSessionResultsFileSecond() {
    Session session = new Session();
    session.session_files = Map.of(SessionFileType.CLASSIFICATION, "3");
    SessionResults sessionResults = new SessionResults();
    sessionResults.file = "2";
    String pdfUrl = SessionService.getPdfUrl(Optional.of(session), Optional.of(sessionResults));
    Assertions.assertThat(pdfUrl).isNotNull().isEqualTo("2");
  }

  @Test
  void getPdfUrlReturnsUrlOfSessionFileThird() {
    Session session = new Session();
    session.session_files = Map.of(SessionFileType.CLASSIFICATION, "3");
    SessionResults sessionResults = new SessionResults();
    String pdfUrl = SessionService.getPdfUrl(Optional.of(session), Optional.of(sessionResults));
    Assertions.assertThat(pdfUrl).isNotNull().isEqualTo("3");
  }
}