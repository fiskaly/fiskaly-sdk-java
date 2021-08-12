package com.fiskaly.sdk;

import static org.junit.Assert.*;

import com.fiskaly.sdk.params.ParamConfig;
import com.fiskaly.sdk.results.ResultSelfTest;
import com.fiskaly.sdk.results.ResultVersion;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class FiskalyHttpClientTest {
  private static final String API_KEY = System.getenv("FISKALY_API_KEY");
  private static final String API_SECRET = System.getenv("FISKALY_API_SECRET");

  public FiskalyHttpClient createClient()
      throws IOException, URISyntaxException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    return new FiskalyHttpClient(
        API_KEY,
        API_SECRET,
        "https://kassensichv.fiskaly.com/api/v2",
        "https://kassensichv-middleware.fiskaly.com");
  }

  @Test
  public void versionTest() throws FiskalyException, URISyntaxException, IOException {
    FiskalyHttpClient client = this.createClient();

    assertNotNull(client);
    final ResultVersion version = client.version();
    assertNotNull(version);
  }

  @Test
  public void selfTestTest() throws FiskalyException, URISyntaxException, IOException {
    FiskalyHttpClient client = this.createClient();

    assertNotNull(client);
    final ResultSelfTest selfTest = client.selfTest();
    assertNotNull(selfTest);
  }

  @Test
  public void echoTest() throws FiskalyException, URISyntaxException, IOException {
    FiskalyHttpClient client = this.createClient();

    assertNotNull(client);
    String utf8test = "/this/is/my/utf8/string/äöü+#*'_-?ß!§$%&/()=<>|";
    final Object echo = client.echo(utf8test);
    assertNotNull(echo);
    assertEquals(utf8test, echo);
  }

  @Test
  public void configTest() throws FiskalyException, URISyntaxException, IOException {
    FiskalyHttpClient client = this.createClient();

    assertNotNull(client);
    final ParamConfig.Config config = client.config(3, "~/tmp/", 1000, 1000, "");
    assertNotNull(config);
    assertEquals(3, config.debugLevel);
    assertEquals("~/tmp/", config.debugFile);
    assertEquals(1000, config.clientTimeout);
    assertEquals(1000, config.smaersTimeout);
    assertEquals("", config.httpProxy);
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

    assertEquals(404, thrown.getStatus());
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

  @Test
  public void queryArray()
      throws IOException, URISyntaxException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    final FiskalyHttpClient client = this.createClient();

    Map<String, String[]> query =
        new HashMap<String, String[]>() {
          {
            put("states", new String[] {"INITIALIZED", "DISABLED"});
          }
        };
    final FiskalyHttpResponse res = client.request("GET", "/tss", null, query);

    assertNotNull(res);
    assertNotNull(res.body);
    assertNotNull(res.headers);
    assertTrue(res.status != 0);
  }
}
