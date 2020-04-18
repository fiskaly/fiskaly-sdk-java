package com.fiskaly.sdk;

public class FiskalyException extends Exception {
  private static final long serialVersionUID = 1L;

  public FiskalyException(final String message) {
    super(message);
  }
}
