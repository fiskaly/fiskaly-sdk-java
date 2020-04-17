package com.fiskaly.sdk.jsonrpc;

public class JsonRpcError {
  public final int code;
  public final String message;
  public final Object data;

  public JsonRpcError(final int code, final String message, final Object data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  @Override
  public String toString() {
    return "JsonRpcError{"
        + "code="
        + code
        + ", message='"
        + message
        + '\''
        + ", data="
        + data
        + '}';
  }
}
