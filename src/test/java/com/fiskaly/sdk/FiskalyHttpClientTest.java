package com.fiskaly.sdk;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class FiskalyHttpClientTest {
  private static final String API_KEY = System.getenv("FISKALY_API_KEY");
  private static final String API_SECRET = System.getenv("FISKALY_API_SECRET");

  public FiskalyHttpClient createClient()
      throws IOException, URISyntaxException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    return new FiskalyHttpClient(API_KEY, API_SECRET, "https://kassensichv.io/api/v1");
  }

  @Test
  public void requestBasic() throws FiskalyException, URISyntaxException, IOException {
    FiskalyHttpClient client = this.createClient();

    assertNotNull(client);
    final FiskalyHttpResponse res = client.request("GET", "/tss");
    assertNotNull(res);
    assertNotNull(res.body);
    assertNotNull(res.headers);
    assertTrue(res.status != 0);
  }

  @Ignore("TODO: re-enable this test with client version 1.1.400!")
  @Test()
  public void requestFail()
      throws IOException, URISyntaxException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    final FiskalyHttpClient client = this.createClient();

    final FiskalyHttpException thrown =
        assertThrows(
            FiskalyHttpException.class,
            new ThrowingRunnable() {
              @Override
              public void run() throws Throwable {
                client.request("POST", "/");
              }
            });

    System.err.println(thrown);
    assertNotNull(thrown);
    assertNotNull(thrown.getMessage());
    assertNotNull(thrown.getCode());
    assertNotNull(thrown.getError());
    assertNotNull(thrown.getRequestId());

    assertEquals(thrown.getStatus(), 400);
  }

  @Test()
  public void wrongMethod()
      throws IOException, URISyntaxException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    final FiskalyHttpClient client = createClient();

    final FiskalyClientException thrown =
        assertThrows(
            FiskalyClientException.class,
            new ThrowingRunnable() {
              @Override
              public void run() throws Throwable {
                client.request("WRONG", "/");
              }
            });

    assertNotNull(thrown);
    assertNotNull(thrown.getMessage());

    assertEquals(thrown.getCode(), -32602);
  }
}
