package com.fiskaly.sdk.params;

import static org.junit.Assert.*;

import com.fiskaly.sdk.factories.GsonFactory;
import com.google.gson.Gson;
import java.util.Collections;
import org.junit.Test;

public class ParamRequestTest {
  private static final Gson GSON = GsonFactory.createGson();

  @Test
  public void missingContext() {
    try {
      new ParamRequest(null, null);
      fail("Expected an IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Missing or empty \"context\" parameter", e.getMessage());
    }
  }

  @Test
  public void missingRequest() {
    try {
      new ParamRequest("foo", null);
      fail("Expected an IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Missing \"request\" parameter", e.getMessage());
    }
  }

  @Test
  public void missingMethod() {
    try {
      new ParamRequest("foo", new ParamRequest.Request(null, null, null, null, null, null));
      fail("Expected an IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Missing or empty \"method\" parameter", e.getMessage());
    }
  }

  @Test
  public void missingPath() {
    try {
      new ParamRequest("foo", new ParamRequest.Request("GET", null, null, null, null, null));
      fail("Expected an IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Missing \"path\" parameter", e.getMessage());
    }
  }

  @Test
  public void bodyEncoding() {
    final byte[] body = "baz".getBytes();
    final ParamRequest r =
        new ParamRequest("foo", new ParamRequest.Request("GET", "bar", body, null, null, null));
    assertNotNull(r);
    assertNotNull(r.request.body);
    assertEquals("YmF6", r.request.body); // base64-encoded "bar"
  }

  @Test
  public void basicRequest() {
    final ParamRequest r =
        new ParamRequest("foo", new ParamRequest.Request("GET", "bar", null, null, null, null));
    assertNotNull(r);
    assertNotNull(r.context);
    assertNotNull(r.request.method);
    assertNotNull(r.request.path);
  }

  @Test
  public void basicRequestJson() {
    final ParamRequest r =
        new ParamRequest("foo", new ParamRequest.Request("GET", "bar", null, null, null, null));
    final String json = GSON.toJson(r);
    assertEquals("{\"context\":\"foo\",\"request\":{\"method\":\"GET\",\"path\":\"bar\"}}", json);
  }

  @Test
  public void emptyQuery() {
    final ParamRequest r =
        new ParamRequest(
            "foo",
            new ParamRequest.Request(
                "GET",
                "bar",
                "baz".getBytes(),
                Collections.<String, Object>emptyMap(),
                null,
                null));
    assertNotNull(r);
    assertNull(r.request.query);
  }

  @Test
  public void emptyHeaders() {
    final ParamRequest r =
        new ParamRequest(
            "foo",
            new ParamRequest.Request(
                "GET",
                "bar",
                "baz".getBytes(),
                Collections.singletonMap("foo", "bar"),
                Collections.<String, String>emptyMap(),
                null));
    assertNotNull(r);
    assertNull(r.request.headers);
  }

  @Test
  public void fullRequest() {
    final ParamRequest r =
        new ParamRequest(
            "foo",
            new ParamRequest.Request(
                "GET",
                "bar",
                "baz".getBytes(),
                Collections.singletonMap("foo", "bar"),
                Collections.singletonMap("baz", "qux"),
                null));
    assertNotNull(r);
    assertNotNull(r.context);
    assertNotNull(r.request.method);
    assertNotNull(r.request.path);
    assertNotNull(r.request.query);
    assertNotNull(r.request.headers);
  }

  @Test
  public void fullRequestJson() {
    final ParamRequest r =
        new ParamRequest(
            "foo",
            new ParamRequest.Request(
                "GET",
                "bar",
                "baz".getBytes(),
                Collections.singletonMap("foo", "bar"),
                Collections.singletonMap("baz", "qux"),
                null));
    final String json = GSON.toJson(r);
    assertEquals(
        "{\"context\":\"foo\",\"request\":{\"method\":\"GET\",\"path\":\"bar\",\"body\":\"YmF6\",\"query\":{\"foo\":\"bar\"},\"headers\":{\"baz\":\"qux\"}}}",
        json);
  }
}
