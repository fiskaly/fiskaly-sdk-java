package com.fiskaly.sdk.jsonrpc;

import java.util.UUID;

public class JsonRpcRequest {
  public final String jsonrpc = "2.0";
  public final String id;
  public final String method;
  public final Object params;

  public JsonRpcRequest(final String method, final Object params) {
    if (method == null || method.isEmpty()) {
      throw new IllegalArgumentException("Missing or empty \"method\" parameter");
    }

    this.method = method;
    this.params = params;
    this.id = UUID.randomUUID().toString();
  }

  public JsonRpcRequest(final String method) {
    this(method, null);
  }

  @Override
  public String toString() {
    return "JsonRpcRequest{"
        + "jsonrpc='"
        + jsonrpc
        + '\''
        + ", id='"
        + id
        + '\''
        + ", method='"
        + method
        + '\''
        + ", params="
        + params
        + '}';
  }
}
