package org.teknichrono.mgp.api.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.teknichrono.mgp.business.service.SessionService;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TestSessionEndpoint {

  @InjectMocks
  private SessionEndpoint sessionEndpoint;

  @Mock
  private SessionService sessionService;

  @BeforeEach
  public void prepare() {
    Mockito.when(sessionService.getSessionByName(2021, "QAT", "MOTOGP", "FP1")).thenReturn(Optional.empty());
  }

  @Test
  public void notFoundExceptionIfSessionNotFound() {
    assertThrows(NotFoundException.class, () -> sessionEndpoint.getSession(2021, "QAT", "MOTOGP", "FP1"));
  }

}