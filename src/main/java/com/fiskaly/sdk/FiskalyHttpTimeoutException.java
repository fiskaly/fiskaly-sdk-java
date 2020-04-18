package com.fiskaly.sdk;

public class FiskalyHttpTimeoutException extends FiskalyException {
  private static final long serialVersionUID = 1L;

  public FiskalyHttpTimeoutException(String message) {
    super(message);
  }
}
