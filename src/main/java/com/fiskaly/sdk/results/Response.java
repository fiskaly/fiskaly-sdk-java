package com.fiskaly.sdk.results;

import java.util.List;
import java.util.Map;

public class Response {
  public final int status;
  public final String body;
  public final Map<String, List<?>> headers;

  public Response(final int status, final String body, final Map<String, List<?>> headers) {
    this.status = status;
    this.body = body;
    this.headers = headers;
  }

  @Override
  public String toString() {
    return "Response{"
        + "status="
        + status
        + ", body='"
        + body
        + '\''
        + ", headers="
        + headers
        + '}';
  }
}
