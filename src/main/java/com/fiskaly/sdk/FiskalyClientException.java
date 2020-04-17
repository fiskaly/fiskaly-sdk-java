package com.fiskaly.sdk;

public class FiskalyClientException extends FiskalyException {
  private final int code;
  private final Object data;

  public FiskalyClientException(int code, String message, Object data) {
    super(message);

    this.code = code;
    this.data = data;
  }

  public int getCode() {
    return this.code;
  }

  public Object getData() {
    return this.data;
  }

  @Override
  public String toString() {
    return "FiskalyClientException{ Code="
        + this.getCode()
        + ", Message="
        + this.getMessage()
        + ", Data="
        + this.getData()
        + "}";
  }
}
