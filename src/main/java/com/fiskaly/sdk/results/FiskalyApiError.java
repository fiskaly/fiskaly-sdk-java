package com.fiskaly.sdk.results;

public class FiskalyApiError {
  public final int statusCode;
  public final String error;
  public final String code;
  public final String message;

  public FiskalyApiError(int statusCode, String error, String code, String message) {
    this.statusCode = statusCode;
    this.error = error;
    this.code = code;
    this.message = message;
  }
}
