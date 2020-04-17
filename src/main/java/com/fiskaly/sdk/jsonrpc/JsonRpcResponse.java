package com.fiskaly.sdk.jsonrpc;

public class JsonRpcResponse<T> {
  public final String id;
  public final JsonRpcError error;
  public final T result;

  public JsonRpcResponse(final String id, final JsonRpcError error, final T result) {
    this.id = id;
    this.error = error;
    this.result = result;
  }

  @Override
  public String toString() {
    return "JsonRpcResponse{"
        + "id='"
        + id
        + '\''
        + ", error="
        + error
        + ", result="
        + result
        + '}';
  }
}
