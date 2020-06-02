package com.fiskaly.sdk;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import com.fiskaly.sdk.params.ParamConfig;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class FiskalyHttpClientTest {
  private static final String API_KEY = System.getenv("API_KEY");
  private static final String API_SECRET = System.getenv("API_SECRET");

  public FiskalyHttpClient createClient()
      throws IOException, URISyntaxException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    return new FiskalyHttpClient(API_KEY, API_SECRET, "https://kassensichv.io/api/v1");
  }

  @Test
  public void configTest() throws URISyntaxException, FiskalyHttpTimeoutException, FiskalyHttpException, FiskalyClientException, IOException {
      FiskalyHttpClient client = this.createClient();

      assertNotNull(client);
      final ParamConfig.Config config = client.config(3, "~/tmp/", 1000, 1000);
      assertNotNull(config);
      assertEquals(3, config.debug_level);
      assertEquals("~/tmp/", config.debug_file);
      assertEquals(1000, config.client_timeout);
      assertEquals(1000, config.smaers_timeout);
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

    assertNotNull(thrown);
    assertNotNull(thrown.getMessage());
    assertNotNull(thrown.getCode());
    assertNotNull(thrown.getError());
    assertNotNull(thrown.getRequestId());

    assertEquals(400, thrown.getStatus());
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

    assertEquals(-32602, thrown.getCode());
  }

  @Test
  public void ghIssue7()
      throws IOException, URISyntaxException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    final FiskalyHttpClient client = createClient();

    // assert that request does NOT throw com.google.gson.JsonSyntaxException:
    final FiskalyHttpException thrown =
        assertThrows(
            FiskalyHttpException.class,
            new ThrowingRunnable() {
              @Override
              public void run() throws Throwable {
                final String body =
                    "{\"state\":\"ACTIVE\",\"client_id\":\"44ad060c-9fed-11ea-a10e-ef72f9ea92f4\",\"schema\":{\"standard_v1\":{\"receipt\":{\"INVALID\":true}}}}";
                client.request(
                    "PUT",
                    "/tss/b5683d54-9fec-11ea-b751-238e39c200c5/tx/bc7cb052-9fec-11ea-b697-fb0858867380",
                    body.getBytes());
              }
            });
    assertEquals(400, thrown.getStatus());
  }
}
