package com.fiskaly.sdk.params;

import java.util.Collections;
import java.util.Map;
import net.iharder.Base64;

public class ParamRequest {
  public final String context;
  public final Request request;

  public ParamRequest(final String context, final Request request) {
    if (context == null || context.isEmpty()) {
      throw new IllegalArgumentException("Missing or empty \"context\" parameter");
    }

    if (request == null) {
      throw new IllegalArgumentException("Missing \"request\" parameter");
    }

    this.context = context;
    this.request = request;
  }

  public static class Request {
    public final String method;
    public final String path;
    public final String body;
    public final Map<String, ?> query;
    public final Map<String, String> headers;
    public final String destinationFile;

    public Request(
        final String method,
        final String path,
        final byte[] body,
        final Map<String, ?> query,
        final Map<String, String> headers,
        final String destinationFile) {
      if (method == null || method.isEmpty()) {
        throw new IllegalArgumentException("Missing or empty \"method\" parameter");
      }

      if (path == null) {
        throw new IllegalArgumentException("Missing \"path\" parameter");
      }

      this.method = method;
      this.path = path;
      this.body = (body != null && body.length != 0) ? Base64.encodeBytes(body) : null;
      this.query = (query != null && !query.isEmpty()) ? Collections.unmodifiableMap(query) : null;
      this.headers =
          (headers != null && !headers.isEmpty()) ? Collections.unmodifiableMap(headers) : null;
      this.destinationFile = destinationFile;
    }
  }
}
