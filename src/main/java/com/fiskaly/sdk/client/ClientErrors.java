package com.fiskaly.sdk.client;

public abstract class ClientErrors {
  public static final int HTTP_ERROR = -20000;
  public static final int HTTP_TIMEOUT_ERROR = -21000;

  private ClientErrors() {}
}
