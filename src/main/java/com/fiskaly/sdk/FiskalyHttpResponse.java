package com.fiskaly.sdk;

import com.fiskaly.sdk.results.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.iharder.Base64;

public class FiskalyHttpResponse {
  public final int status;
  public final byte[] body;
  public final Map<String, List<?>> headers;

  public FiskalyHttpResponse(final Response response) throws IOException {
    this.status = response.status;
    this.body = Base64.decode(response.body);
    this.headers = response.headers;
  }

  @Override
  public String toString() {
    return "FiskalyClientResponse{"
        + "status="
        + status
        + ", body='"
        + new String(body)
        + '\''
        + ", headers="
        + headers
        + '}';
  }
}
