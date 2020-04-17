package com.fiskaly.sdk;

public class FiskalyHttpException extends FiskalyException {
  public final int status;
  public final String error;
  public final String code;
  public final String requestId;

  public FiskalyHttpException(
      int status, String error, String message, String code, String requestId) {
    super(message);

    this.status = status;
    this.error = error;
    this.code = code;
    this.requestId = requestId;
  }

  public int getStatus() {
    return this.status;
  }

  public String getError() {
    return this.error;
  }

  public String getCode() {
    return this.code;
  }

  public String getRequestId() {
    return this.requestId;
  }

  @Override
  public String toString() {
    return "{ Status: \""
        + this.status
        + "\", Error: \""
        + this.error
        + "\", Code: \""
        + this.code
        + "\""
        + ", RequestId: \""
        + this.requestId
        + "\"}";
  }
}
